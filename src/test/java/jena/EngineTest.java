package jena;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;

import Query.StarQuery;
import Query.StarQueryParser;
import Query.Result;
import hmin313.rdf_star_engine.RDF_StarEngine;
import jena.JenaQueryEngine;

public class EngineTest {

//	File file = new File("data/opsIndex.txt");
//	file.createNewFile();
//	FileOutputStream fos = new FileOutputStream(file,false);
//	Index.displayDatas(pos,fos);
//	fos.close();
	
	public static RDF_StarEngine engine;
	public static StarQueryParser queryParser;
	public static TreeSet<String> queriesPath;
	public static String dataDir,ext;
	public static JenaQueryEngine jenaEngine;
	
	@BeforeClass
	public static void setup() throws RDFParseException, RDFHandlerException, IOException {
		engine = new RDF_StarEngine("data/500K.rdfxml","");
		queryParser = new StarQueryParser(engine.getDictionnary());
		
	    File queryDir = new File("data/queries");
	    queriesPath = new TreeSet<>();
	    for(File fileName : queryDir.listFiles()) {
	    	queriesPath.add("data/queries/"+fileName.getName());
	    }
	    
//	    dataDir="data/queries/";
//	    ext = ".queryset";    
//		queriesPath = new TreeSet<>(Arrays.asList(
//			   dataDir+"Q_1_likes.queryset",
//			   dataDir+"Q_2_likes_nationality.queryset",
//			   dataDir+"Q_4_location_nationality_gender_type"+ext
//		));

		jenaEngine = new JenaQueryEngine("data/500K.rdfxml");
		
	}
	
	@Test
	public void testQuerying() throws RDFParseException, RDFHandlerException, IOException {

		long totalStarTime = 0, totalJenaTime = 0;
		Instant t1;
			
		for(String queryPath : queriesPath) {
			System.out.println("\n\n"+queryPath+" :");
			List<StarQuery> queries = queryParser.readQueries(queryPath);	
			assertEquals(queries.size(), 100);
			
			for(StarQuery query : queries) {	
//				System.out.println("\n"+query);
				
				ArrayList<String> starEngineResults = new ArrayList<>();
				
				t1 = Instant.now();				
				Collection<Integer> results = engine.runStarQuery(query);				
				long ellapsedTime = Duration.between(t1,Instant.now()).toMillis();
				

				if(! results.isEmpty()) {
					Result starQueryResult = new Result(engine.getDictionnary(), results);
					starEngineResults = starQueryResult.getResults();
					
//					System.out.println("\trdf_star :"+results.size()+" answers found, "+ "time="+ellapsedTime+"ms [OK]");
//					for(String res : starEngineResults) {
//						System.out.println("\t\t"+res);
//					}
				}
				totalStarTime += ellapsedTime;
				
				t1 = Instant.now();
				ArrayList<String> jenaResults = jenaEngine.getResults(query);	
				ellapsedTime = Duration.between(t1,Instant.now()).toMillis();
				totalJenaTime += ellapsedTime;
				
				if(!jenaResults.isEmpty()) {
//					System.out.println("\tjena : "+jenaResults.size()+" answers found, time="+ellapsedTime+"ms [OK]\n");
//					for(String res : jenaResults) {
//						System.out.println("\t\t"+res);
//					}
				}
				
				HashSet<String> s1=null,s2 = null;
				if(starEngineResults != null)
					s1 = new HashSet<>(starEngineResults);
				if(jenaResults != null)
					s2 = new HashSet<>(jenaResults);
				
				assertEquals(s1,s2);
				
			}
		}
		System.out.println("Total StarEngine querying time : "+totalStarTime+"ms [OK]");
		System.out.println("Total Jena querying time : "+totalJenaTime+"ms [OK]");
		
		
	}
	
	
}
