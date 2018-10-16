package hmin313.rdf_star_engine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import Dictionary.Dictionary;
import Index.Index;
import Index.OPSIndex;
import Index.POSIndex;

public class RDF_StarEngine {
	
	private Dictionary dictionnary;
	private Index opsIndex,posIndex;
	private boolean verbose;
	

	
	public RDF_StarEngine(String dataPath, boolean verbose) throws RDFParseException, RDFHandlerException, IOException {
		this.verbose = verbose;
		loadData(dataPath);
	}
	
	private void loadData(String path) throws RDFParseException, RDFHandlerException, IOException{   	
		
		Instant t1 = Instant.now();

		Reader reader = new FileReader(path);
		RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		Stored_RDFListener listenner = new Stored_RDFListener();
		rdfParser.setRDFHandler(listenner);
		rdfParser.parse(reader, "");
		reader.close();	
		
		ArrayList<ArrayList<String>> triples = listenner.getTriples();
		if(verbose) {

			File file = new File(path);
			System.out.println("Reading "+triples.get(0).size()+" triples[OK]\n"
					+"\ttime="+Duration.between(t1,Instant.now()).toMillis()+"ms, total_size="+file.length()/(1024*1024)+"MB");
			t1 = Instant.now();
		}
					
		dictionnary = new Dictionary(listenner.getTerms());
		if(verbose) {
			
			System.out.println("Dictionary Build [OK] \n"
					+"\ttime="+Duration.between(t1,Instant.now()).toMillis()+"ms, total_size=");
			t1 = Instant.now();
		}
		
		opsIndex = new OPSIndex(dictionnary,triples);
		posIndex = new POSIndex(dictionnary,triples);
		if(verbose) {
			System.out.println("Index Build [OK] time="+Duration.between(t1,Instant.now()).toMillis()+"ms");
		}
	}
	
	

}
