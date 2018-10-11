package hmin313.rdf_star_engine;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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
	
	public RDF_StarEngine(String dataPath) throws RDFParseException, RDFHandlerException, IOException {
		loadData(dataPath);
	}
	
	private void loadData(String path) throws RDFParseException, RDFHandlerException, IOException{   	
		
		Reader reader = new FileReader(path);
		RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		Stored_RDFListener listenner = new Stored_RDFListener();
		rdfParser.setRDFHandler(listenner);
		rdfParser.parse(reader, "");
		reader.close();	
		
		dictionnary = new Dictionary(listenner.getTerms());
		ArrayList<ArrayList<String>> triples = listenner.getTriples();
		opsIndex = new OPSIndex(dictionnary,triples);
		posIndex = new POSIndex(dictionnary,triples);
	}

}
