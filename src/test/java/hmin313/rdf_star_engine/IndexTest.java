package hmin313.rdf_star_engine;

import static org.junit.Assert.assertEquals;
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

import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import Dictionary.Dictionary;
import Index.Index;
import Index.OPSIndex;
import Index.POSIndex;

public class IndexTest {
	
	private static Dictionary dico;
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
		termList = listenner.getTerms().keySet();
		triples = listenner.getTriples();
		int triple_size = triples.get(0).size();
		
		System.out.println("Reading "+triple_size+" triples\n"
				+"\ttime="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
		
	}

	
	@Test
	public void testIndexCreation(){
		Instant t1 = Instant.now();
		Dictionary trieDico = new Dictionary(termList);
		System.out.println("PatriciaTrieDico Building time="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
		
		t1=Instant.now();
		posIndex = new POSIndex(trieDico, triples);
		System.out.println("POS Index Building [OK]");
		System.out.println("\tsize="+posIndex.nbTriple()+" time="+Duration.between(t1,Instant.now()).toMillis()+"ms");
		
		t1=Instant.now();
		opsIndex = new OPSIndex(trieDico, triples);
		System.out.println("OPS Index Building [OK]");
		System.out.println("\tsize="+opsIndex.nbTriple()+" time="+Duration.between(t1,Instant.now()).toMillis()+"ms");
//		Index.displayDatas(posIndex);
	}
	
	public void testIndexQuerying(){
		String[][] parts = {
				{"http://purl.org/dc/terms/Location","http://db.uwaterloo.ca/~galuc/wsdbm/City1"},
				{"http://schema.org/nationality","http://db.uwaterloo.ca/~galuc/wsdbm/Country24"},
				{"http://db.uwaterloo.ca/~galuc/wsdbm/gender","http://db.uwaterloo.ca/~galuc/wsdbm/Gender1"},
				{"http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://db.uwaterloo.ca/~galuc/wsdbm/Role2"}
		};
		
		for(String[] part : parts){
			String s1 = part[0];
			String s2 = part[1];
			
		}
 
		
	}
	
	@Test
	public void testHashOrTreeMap() {
		
		Random rand = new Random(516456466556L);
		int n = 1000000;
		int bound = 2000000000;
		
		Set<Integer> m1 = new HashSet<>(n), m2 = new TreeSet<>();
		int [] data = new int[n];
		for(int i=0;i<n;i++) {
			data[i] = rand.nextInt(bound);
		}
		
		Instant t1 = Instant.now();
		for(int x : data) {
			m1.add(x);
		}
		System.out.println("HashSet build time="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");

		
		t1 = Instant.now();
		for(int x : data) {
			m2.add(x);
		}
		System.out.println("TreeSet build time="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
		assertEquals(m1, m2);
		
		
		t1 = Instant.now();
		for(Integer key : m2) {
			m1.contains(key);
		}
		System.out.println("HashSet get time="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");

		
		t1 = Instant.now();
		for(Integer key : m1) {
			m2.contains(key);
		}
		System.out.println("TreeSet get time="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
		
	}
	
	

}
