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

import Dictionary.Dictionary;
import Index.*;

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
		
		System.out.println("\tDataset size:"+totalSize+"B\n");
		System.out.println("\tReading time="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");

		totalSize = 0;
		dico = new Dictionary(listenner.getTerms());
		
	
	@Test
	public void testDicoMatching(){
		System.out.println("[START]");
		Instant t1 = Instant.now();
		queryMain("data/queries/Q_1_eligibleregion.queryset", dico, );
		System.out.println("Matching "+triple_size+" ids\n"
					+"\ttime="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
	
	}
	
		@Test
	public void testQuerying(){
	
			Instant t1 = Instant.now();
	
			System.out.println("Reading "+triple_size+" triples\n"
				+"\ttime="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
	
	}
	
}