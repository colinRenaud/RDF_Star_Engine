package Query;

import org.apache.commons.collections4.trie.PatriciaTrie;

public class ResultSet {
	
	PatriciaTrie<Result> resultsByQueryName;
	long totalTime,resultsSize;

	
	public ResultSet(PatriciaTrie<Result> results) {
		super();
		this.resultsByQueryName = results;
		
		totalTime = 0;
		resultsSize = 0;
		for(String q : results.keySet()) {		
			totalTime += results.get(q).getTime();	
			resultsSize += results.get(q).getResults().size();
		}

	}

	public PatriciaTrie<Result> getResults() {
		return resultsByQueryName;
	}

	public long getQueryNb() {
		return resultsByQueryName.size();
	}
	
	public long getResultNb() {
		return resultsSize;
	}
	
	public long getTotalTime() {	
		return totalTime;
	}
	
	

}
