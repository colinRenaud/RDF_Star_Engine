package hmin313.rdf_star_engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.commons.io.FilenameUtils;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import com.hp.hpl.jena.sparql.function.library.sqrt;

import Dictionary.Dictionary;
import Index.Tri_Index;
import Index.Index;
import Index.OPSIndex;
import Index.POSIndex;
import Query.StarQuery;
import Query.StarQueryParser;
import Query.Result;
import Query.ResultSet;

public class RDF_StarEngine {
	
	

	private Dictionary dictionnary;
	private Tri_Index opsIndex,posIndex;
	private Index index;
	private boolean verbose;
	private StarQueryParser queryParser;
	/**
	 * 
	 * @param dataPath
	 * @param outputDir
	 * @throws RDFParseException
	 * @throws RDFHandlerException
	 * @throws IOException
	 */
	public RDF_StarEngine(String dataPath,String outputDir) throws RDFParseException, RDFHandlerException, IOException {
		this(dataPath,false,false,outputDir);		
	}
	
	
	/**
	 * 
	 * @param dataPath
	 * @param verbose
	 * @param export_results
	 * @param export_stats
	 * @param workload_time
	 * @param outputDir
	 * @throws RDFParseException
	 * @throws RDFHandlerException
	 * @throws IOException
	 */
	public RDF_StarEngine(String dataPath,boolean verbose, boolean workload_time,
			String outputDir) throws RDFParseException, RDFHandlerException, IOException {
		this.verbose = verbose;
		loadData(dataPath);
	    
	}



	public Dictionary getDictionnary() {
		return dictionnary;
	}

	public Tri_Index getOpsIndex() {
		return opsIndex;
	}

	public Tri_Index getPosIndex() {
		return posIndex;
	}

	/**
	 * 
	 * @param path
	 * @throws RDFParseException
	 * @throws RDFHandlerException
	 * @throws IOException
	 */
	private void loadData(String path) throws RDFParseException, RDFHandlerException, IOException{   	
		
		Instant t1 = Instant.now();
	
		Reader reader = new FileReader(path);
		RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		Stored_RDFListener listenner = new Stored_RDFListener();
		rdfParser.setRDFHandler(listenner);
		rdfParser.parse(reader, "");
		reader.close();	
		
		ArrayList<ArrayList<String>> triples = listenner.getTriples();
		
		File file = new File(path);
		System.out.println("Reading "+triples.get(0).size()+" triples[OK] "
				+"\ttime="+Duration.between(t1,Instant.now()).toMillis()+"ms, total_size="+file.length()/(1024*1024)+"MB");
		
		t1 = Instant.now();					
		dictionnary = new Dictionary(listenner.getTerms());		
		System.out.println("Dictionary Building [OK] "
				+"\ttime="+Duration.between(t1,Instant.now()).toMillis()+"ms, entries_nb="+dictionnary.length());
		
//		t1 = Instant.now();
//		index = new Index(dictionnary, triples);
//		System.out.println("Indexs Building [OK] time="+Duration.between(t1,Instant.now()).toMillis()+"ms");

		t1 = Instant.now();		
		opsIndex = new OPSIndex(dictionnary,triples);
		posIndex = new POSIndex(dictionnary,triples);
		System.out.println("TriIndexs Building [OK] time="+Duration.between(t1,Instant.now()).toMillis()+"ms");
		System.out.println("\t{OPS,POS} Index size="+posIndex.getLength());
		queryParser = new StarQueryParser(dictionnary);
	
	}
	
	
	
	

	
	/**
	 * 
	 * @param queryDirPath
	 * @throws IOException
	 */
	public ResultSet runQueryInDir(String queryDirPath) throws IOException {
		
		File queryDir = new File(queryDirPath);
		if(! queryDir.exists()) {
			throw new FileNotFoundException(queryDirPath);
		}
		
		TreeSet<String> queryFilePaths = new TreeSet<>();
	    for(File fileName : queryDir.listFiles()) {
	    	queryFilePaths.add(fileName.getName());
	    }
	    if(queryFilePaths.isEmpty()) {
	    	throw new IllegalArgumentException("No query file found into "+queryDirPath);
	    }	    	     
	    PatriciaTrie<Result> queriesResults = new PatriciaTrie<>();
//	    HashMap<StarQuery,Long> queriesTime = new LinkedHashMap<>();
	    
	    for(String queryPath : queryFilePaths) {
	    	
	    	Instant t1 = Instant.now();
			
			List<StarQuery> queries = queryParser.readQueries(queryDirPath+queryPath);				
			if(queries.isEmpty()) {
				throw new IllegalArgumentException("No query found into "+queryPath);
			}	    		
			
			int newResultNb = 0,uniqueQueryNb=0;
			
			for(StarQuery query : queries) {	
				
				Instant queryingTime = Instant.now();				
				String queryName = query.toOutputString();			
				Result sqr = queriesResults.get(queryName);

				if(sqr == null) { // new query asked
					Collection<Integer> results = runStarQuery(query);
					sqr = new Result(dictionnary,results); // convert results as integers to results as strings	
					queriesResults.put(queryName,sqr);	
					newResultNb += results.size();
					uniqueQueryNb++;
				}
				Duration d1 = Duration.between(queryingTime,Instant.now());
				sqr.setTime(d1.toNanos());				
			}		
			
			long pathEvalTime = Duration.between(t1, Instant.now()).toMillis();
			if(verbose) {
				System.out.println(queryPath+ " : \n\t"
						+uniqueQueryNb+"/"+queries.size()+" evaluated queries, "
						+newResultNb+" answers found,"
						+"time="+pathEvalTime+"ms");
			}
	    }
	    return new ResultSet(queriesResults);
	}
	
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	public Collection<Integer> runStarQuery(StarQuery query){
		ArrayList<Integer> queryPredIds = query.getPredicatesIds(),
							queryObjIds = query.getObjectsIds();
		
			
		if(queryPredIds.size() == 1) { // simple star query with one branch -> result is : Index I : {PO}[S]all subjects from some index[predId,objectId]
			Integer predId = queryPredIds.get(0), objId = queryObjIds.get(0);
			Set<Integer> results = posIndex.get(predId,objId);
			return results != null ? results :  new ArrayList<>();
		}
		
		Collection<Integer> results = new ArrayList<>();		
		Integer smallerSubIndex = getMostFilteringSubIndex(posIndex, queryPredIds, queryObjIds); // get smaller subIndex to join
		if(smallerSubIndex == -1) {
			return new ArrayList<>();
		}
		Integer srcPredId = queryPredIds.get(smallerSubIndex), 
				srcObjId = queryObjIds.get(smallerSubIndex);
		Set<Integer> subjects = posIndex.get(srcPredId,srcObjId);
		
		if(subjects != null) {

			for(Integer subjId : subjects) { // for each subject try to find it into all other subjectList
				
				boolean joinAllSuccess = true;
				int i = 0;
				while(i<queryPredIds.size() && joinAllSuccess) { // loop each other subjectList
					
					if(i != smallerSubIndex) { // don't Rechecks source subject list
						Integer predId = queryPredIds.get(i), objId = queryObjIds.get(i);
						Set<Integer> subIndexToJoin = posIndex.get(predId,objId);
						if(!subIndexToJoin.contains(subjId)) {
							joinAllSuccess = false; // failed to join with all other subjectList
						}
					}
					i++;
				}
				if(joinAllSuccess) {
					results.add(subjId); // add subjId only if join success 
				}
			}
			
		}
		
		return results;
	}
	
	/**
	 * 
	 * @param index
	 * @param predIds
	 * @param objectIds
	 * @return
	 */
	private Integer getMostFilteringSubIndex(Tri_Index index, ArrayList<Integer> predIds, ArrayList<Integer> objectIds) {
		
		Set<Integer> firstSubIndex = index.get(predIds.get(0),objectIds.get(0));
		if(firstSubIndex == null) {
			return -1;
		}
		int min_size = firstSubIndex.size();
		int size = 0;
		int minIdx = 0;
		for(int i=1;i<predIds.size();i++) {
			Set<Integer> subIndex = index.get(predIds.get(i),objectIds.get(i));	
			if(subIndex == null) {
				return -1;
			}
			size = subIndex.size();
			if(size < min_size) {
				min_size = size;
				minIdx = i;
			}
		}
		return minIdx;
	}
	
}
