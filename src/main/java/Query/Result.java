package Query;

import java.util.ArrayList;
import java.util.Collection;

import Dictionary.Dictionary;


public class Result {
	
	private ArrayList<String> results;
	
	public Result(Dictionary dico, Collection<Integer> results) {
		this.results = new ArrayList<>(results.size());
		for(Integer res : results) {
			this.results.add(dico.getStringId(res));
		}
	}
	
	public ArrayList<String> getResults(){
		return results;
	}
	
	private Long time;

	public void setTime(Long time) {
		this.time = time;
	}

	public Long getTime() {
		return time;
	}
	
	
	
}
