package hmin313.rdf_star_engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FilenameUtils;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import Dictionary.Dictionary;
import Index.Index;
import Index.OPSIndex;
import Index.POSIndex;
import Query.StarQuery;
import Query.StarQueryParser;

public class RDF_StarEngine {
	
	

	private Dictionary dictionnary;
	private Index opsIndex,posIndex;
	private boolean verbose, export_results, export_stats, workload_time;
	private String outputDir;
	private StarQueryParser queryParser;
	
	
	public final static String 
			EXEC_TIME_FILE = "exec_time.csv", 
			QUERY_STATS_FILE = "query_stats",
			QUERY_RESULTS_FILE = "query_results";
	

	/**
	 * 
	 * @param dataPath
	 * @param outputDir
	 * @throws RDFParseException
	 * @throws RDFHandlerException
	 * @throws IOException
	 */
	public RDF_StarEngine(String dataPath,String outputDir) throws RDFParseException, RDFHandlerException, IOException {
		this(dataPath,false,false,false,false,outputDir);		
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
	public RDF_StarEngine(String dataPath,boolean verbose, boolean export_results, boolean export_stats, boolean workload_time,
			String outputDir) throws RDFParseException, RDFHandlerException, IOException {
		super();
		this.verbose = verbose;
		this.export_results = export_results;
		this.export_stats = export_stats;
		this.workload_time = workload_time;
		this.outputDir = outputDir;
		loadData(dataPath);
	    File outputDirFile = new File(outputDir);
	    if(! outputDirFile.exists()) {
	    	outputDirFile.mkdir();
	    }
	}



	public Dictionary getDictionnary() {
		return dictionnary;
	}

	public Index getOpsIndex() {
		return opsIndex;
	}

	public Index getPosIndex() {
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
		
		t1 = Instant.now();		
		opsIndex = new OPSIndex(dictionnary,triples);
		posIndex = new POSIndex(dictionnary,triples);
		System.out.print("Index Building [OK] time="+Duration.between(t1,Instant.now()).toMillis()+"ms");
		System.out.println(" {OPS,POS} Index size="+posIndex.getLength());
		queryParser = new StarQueryParser(dictionnary);
	}
	
	private List<File> createOutputFiles() throws IOException {
	   List<File> files = new LinkedList<>(Arrays.asList(
			  new File(outputDir+EXEC_TIME_FILE), 
			  new File(outputDir+QUERY_RESULTS_FILE),
			  new File(outputDir+QUERY_STATS_FILE)));
	   
	    for(File file : files) {
	    	if(file.exists()) {
	    		file.delete();
	    	}
	    	else {
	    		file.createNewFile();
	    	}
	    }
	    return files;
	}
	
	

	
	/**
	 * 
	 * @param queryDirPath
	 * @throws IOException
	 */
	public void runQueryInDir(String queryDirPath) throws IOException {
		
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
	    
	    createOutputFiles();
	    long totalQueryingTime = 0L;
	    int totalQueryNb = 0, totalResultsNb = 0;	  
	  
	    for(String queryPath : queryFilePaths) {
	    	
			List<StarQuery> queries = queryParser.readQueries(queryDirPath+queryPath);			
			if(queries.isEmpty()) {
				throw new IllegalArgumentException("No query found into"+queryPath);
			}
			if(verbose) {
	    		System.out.println(queryPath + " : "+queries.size()+" queries to process");
	    	}
			
			int queryNb = 0;
			for(StarQuery query : queries) {		
				totalQueryNb++;
				queryNb++;
				Instant queryingTime = Instant.now();				
				Collection<Integer> results = runStarQuery(query);
				long ellapsedTime = Duration.between(queryingTime,Instant.now()).toMillis();
				
				int results_size = results != null ? results.size() : 0;
				totalResultsNb += results_size;
				totalQueryingTime += ellapsedTime;
				
				if(verbose) {					
					System.out.println("\t query"+queryNb+" : "+results_size + " answers found, time="+ellapsedTime+"ms");
				}
				if(export_results) {
					
				}					
			}
	    }
	    if(workload_time) {
	    	System.out.println("Workload_time :"+totalQueryingTime+"ms, "+totalQueryNb+" query run ,"+totalResultsNb+" results found");
	    }
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
					results.add(subjId); // add subjId only if joined succes 
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
	private Integer getMostFilteringSubIndex(Index index, ArrayList<Integer> predIds, ArrayList<Integer> objectIds) {
		
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
