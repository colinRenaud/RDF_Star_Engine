package hmin313.rdf_star_engine;



import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import Dictionary.Dictionary;

public class DictionaryTest {
	
	private static Collection<String> termList;
	
	@BeforeClass
	public static void setup() throws RDFParseException, RDFHandlerException, IOException {
		System.out.println("Reading data [Start]");	
		Reader reader = new FileReader("data/500K.rdfxml");
		RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		Stored_RDFListener listenner = new Stored_RDFListener();
		rdfParser.setRDFHandler(listenner);
		rdfParser.parse(reader, "");
		reader.close();
		termList = listenner.getTerms().keySet();
		System.out.println("Reading data [OK]");	

	}

	
	@Test
	public void testPatriciaTrieAssociation(){
		String[] uris = {"alice","parentOf","bob","knows"};

		Dictionary dico = new Dictionary(Arrays.asList(uris));
		assert(dico.getIntegerId("alice")==0);
		assert(dico.getIntegerId("bob")==1);
		assert(dico.getIntegerId("parentOf")==3);
		assert(dico.getIntegerId("knows")==2);
		
		assert(dico.getStringId(0).equals("alice"));
		assert(dico.getStringId(1).equals("bob"));
		assert(dico.getStringId(2).equals("knows"));
		assert(dico.getStringId(3).equals("parentOf"));
	}
	
	@Test
	public void testPerformances() {
		Instant t1 = Instant.now();		
		Dictionary trieDico = new Dictionary(termList);
		System.out.println("PatriciaTrieDico Building time="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
		
	
		List<String> uris= trieDico.getURIs();
		Collection<Integer> ids = trieDico.getIds();	
		System.out.println(uris.size() + ":"+ids.size());

		t1 = Instant.now();
		for(int i=0;i<5;i++){
			for(String uri : uris) {
				trieDico.getIntegerId(uri);
			}
		}	
		System.out.println("PatriciaTrieDico mapping time="+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");

	}
}
