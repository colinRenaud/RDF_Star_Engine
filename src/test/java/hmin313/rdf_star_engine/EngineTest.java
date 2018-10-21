package hmin313.rdf_star_engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;

import Dictionary.Dictionary;
import Index.Index;
import Query.StarQuery;
import Query.StarQueryParser;

public class EngineTest {

//	File file = new File("data/opsIndex.txt");
//	file.createNewFile();
//	FileOutputStream fos = new FileOutputStream(file,false);
//	Index.displayDatas(pos,fos);
//	fos.close();
	
	public static RDF_StarEngine engine;
	public static StarQueryParser queryParser;
	public static List<String> queriesPath;
	
	@BeforeClass
	public static void setup() throws RDFParseException, RDFHandlerException, IOException {
		engine = new RDF_StarEngine("data/500K.rdfxml","");
		queryParser = new StarQueryParser(engine.getDictionnary());
	    File queryDir = new File("data/queries");
	    queriesPath = new ArrayList<>();
	    for(File fileName : queryDir.listFiles()) {
	    	queriesPath.add("data/queries/"+fileName.getName());
	    }
//		 Arrays.asList(
////				"data/queries/Q_1_likes.queryset"
////			   ,"data/queries/Q_2_likes_nationality.queryset"
//			   "data/queries/Q_2_tag_homepage.queryset"
//				));
	}
	
	@Test
	public void testQuerying() throws RDFParseException, RDFHandlerException, IOException {

		Instant t1 = Instant.now(),t2;		
		
		for(String queryPath : queriesPath) {
			System.out.println("\n\n"+queryPath+" :");
			List<StarQuery> queries = queryParser.readQueries(queryPath);	
			for(StarQuery query : queries) {				
				t2 = Instant.now();
				
				Collection<Integer> results = engine.query(query);
				System.out.print("\t"+query + " : ");
				int results_size = results != null ? results.size() : 0;
				System.out.println(results_size + " answers found, "
						+ "time="+Duration.between(t2,Instant.now()).toMillis()+"ms [OK]");
			}
		}

		System.out.println("\nTotal querying time : "+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
		
		
	}
}
