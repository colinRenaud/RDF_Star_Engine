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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import Query.Query;
import Dictionary.Dictionary;
import Index.*;

public class QueryTest {
	
	/*@BeforeClass
	public static void setup() throws RDFParseException, RDFHandlerException, IOException {
	
	}*/
	
		public static Dictionary dico;
		private static Collection<String> termList;
		private static ArrayList<ArrayList<String>> triples;
		private static Index posIndex, opsIndex;
	
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
		
		System.out.println("\tDataset size:B\n");
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
		termList = listenner.getTerms().keySet();
		triples = listenner.getTriples();
		int triple_size = triples.get(0).size();
		
		System.out.println("Reading triples\n"
				+"\ttime="+Duration.between(t2,Instant.now()).toMillis()+"ms [OK]");
		
		}
	
	@Test
	public void testQuerying(){
		System.out.println("[START]");
		Instant t1 = Instant.now();
		Index ind = new OPSIndex(dico,triples);
		String option = "terminal";
		Query q = new Query("data/queries/Q_1_eligibleregion.queryset", dico, ind, option);
		System.out.println("Matching ids\n"
					+"\ttime="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
	
	}
	
}