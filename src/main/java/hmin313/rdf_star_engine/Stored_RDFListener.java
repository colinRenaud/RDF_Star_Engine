package hmin313.rdf_star_engine;

import java.util.ArrayList;

import org.openrdf.model.Statement;
import org.openrdf.rio.helpers.RDFHandlerBase;

public class Stored_RDFListener extends RDFHandlerBase{
	
	private ArrayList<String> terms;
	
	public Stored_RDFListener() {
		terms = new ArrayList<>();
	}
	
	@Override
	public void handleStatement(Statement st) {
		terms.add(st.getSubject().toString());
		terms.add(st.getPredicate().toString());
		terms.add(st.getObject().toString());		
	}

	public ArrayList<String> getTerms() {
		return terms;
	}

}
