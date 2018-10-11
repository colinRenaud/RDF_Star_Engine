package hmin313.rdf_star_engine;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import Dictionary.Dictionary;
import Index.OPSIndex;
import Index.POSIndex;

public class IndexTest {
	
	private static Collection<String> termList;
	private static ArrayList<ArrayList<String>> triples;

	
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
		termList = listenner.getTerms().keySet();
		triples = listenner.getTriples();
		System.out.println("Reading "+triples.size() +" triples time="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
	}
	
	@Test
	public void testIndexCreation(){
//		Instant t1 = Instant.now();
//		Dictionary trieDico = new Dictionary(termList);
//		System.out.println("PatriciaTrieDico Building time="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
//		
//		t1=Instant.now();
//		POSIndex posIndex = new POSIndex(trieDico, triples);
//		System.out.println("POS Index Building [OK]");
//		System.out.println("\tsize="+posIndex.nbTriple()+" time="+Duration.between(t1,Instant.now()).toMillis()+"ms");
//		
//		t1=Instant.now();
//		OPSIndex opsIndex = new OPSIndex(trieDico, triples);
//		System.out.println("OPS Index Building [OK]");
//		System.out.println("\tsize="+opsIndex.nbTriple()+" time="+Duration.between(t1,Instant.now()).toMillis()+"ms");
		//		Index.displayDatas(posIndex);
	}

}
