package hmin313.rdf_star_engine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import Dictionary.Dictionnary;
import Dictionary.PatriciaTrieDictionary;

public class RDF_StarEngine {
	
	private Dictionnary dictionnary;
	
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
		
		dictionnary = new PatriciaTrieDictionary(listenner.getTerms());
	}

}
