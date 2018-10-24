package hmin313.rdf_star_engine;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import Query.StarQuery;
import Query.StarQueryParser;
import Dictionary.Dictionary;

public class QueryTest {
	
	/*@BeforeClass
	public static void setup() throws RDFParseException, RDFHandlerException, IOException {
	
	}*/
	
	public static Dictionary dico;
	
	@BeforeClass
	public static void setup() throws RDFParseException, RDFHandlerException, IOException {
		System.out.println("Reading data [Start]");	
		Instant t1 = Instant.now();
		Reader reader = new FileReader("data/500K.rdfxml");
		RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		Stored_RDFListener listenner = new Stored_RDFListener();
		rdfParser.setRDFHandler(listenner);
		rdfParser.parse(reader, "");
		reader.close();
		
//		System.out.println("\tDataset size:\n");
		System.out.println("\tReading time="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
		dico = new Dictionary(listenner.getTerms());
		
		System.out.println("Reading data [Start]");	
		
		Instant t2 = Instant.now();
		Reader reader2 = new FileReader("data/500K.rdfxml");
		RDFParser rdfParser2 = Rio.createParser(RDFFormat.RDFXML);
		Stored_RDFListener listenner2 = new Stored_RDFListener();
		rdfParser2.setRDFHandler(listenner2);
		rdfParser2.parse(reader2, "");
		reader2.close();

		System.out.println("Reading triples\n"
				+"\ttime="+Duration.between(t2,Instant.now()).toMillis()+"ms [OK]");
		
		}
	
	@Test
	public void testQuerying() throws IOException{
		System.out.println("Query parsing [START]");
		Instant t1 = Instant.now();
		
		StarQueryParser queryParser = new StarQueryParser(dico);
		List<StarQuery> queries = queryParser.readQueries("data/queries/Q_1_eligibleregion.queryset");
		for(StarQuery query : queries) {
			System.out.println(query);
		}
		
//		List<StarQuery> queries2 = queryParser.readQueries("data/queries/Q_3_location_nationality_gender.queryset");
//		System.out.println("\n"+queries2.toString());
//
		System.out.println("Querying \n"
					+"\ttime="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
	
		
//		String option = "terminal";
//		Query2 q = new Query2("data/queries/Q_1_eligibleregion.queryset", dico, ind, option);
	}
	
}