package hmin313.rdf_star_engine;

import java.util.ArrayList;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.openrdf.model.Statement;
import org.openrdf.rio.helpers.RDFHandlerBase;

public class Stored_RDFListener extends RDFHandlerBase{
	
	private PatriciaTrie<Integer> terms;
	private ArrayList<ArrayList<String>> triples;
	private long totalSize;
	
	public Stored_RDFListener() {
		terms = new PatriciaTrie<>();
		triples = new ArrayList<>(3);
		for(int i=0;i<3;i++){
			triples.add(new ArrayList<>());
		}
		totalSize = 0;
	}
	
	@Override
	public void handleStatement(Statement st) {
		String subj = st.getSubject().toString();
		String pred = st.getPredicate().toString();
		String obj = st.getObject().toString();		
		triples.get(0).add(subj);
		triples.get(1).add(pred);
		triples.get(2).add(obj);
		terms.put(subj,0);
		terms.put(pred,0);
		terms.put(obj,0);	
		totalSize += (subj.length() + pred.length() + obj.length());
	}

	

	public PatriciaTrie<Integer> getTerms() {
		return terms;
	}

	public ArrayList<ArrayList<String>> getTriples() {
		return triples;
	}

	public long getTotalSize() {
		return totalSize;
	}
	
	
	
	

}
