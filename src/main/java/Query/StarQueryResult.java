package Query;

import java.util.ArrayList;
import java.util.Collection;

import Dictionary.Dictionary;


public class StarQueryResult {
	
	private ArrayList<String> results;
	
	public StarQueryResult(Dictionary dico, Collection<Integer> results) {
		this.results = new ArrayList<>(results.size());
		for(Integer res : results) {
			this.results.add(dico.getStringId(res));
		}
	}
	
	public ArrayList<String> getResults(){
		return results;
	}
	
}
