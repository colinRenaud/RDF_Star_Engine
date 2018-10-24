package Index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import Dictionary.Dictionary;

public class POSIndex extends Index{
	
	private Map<Integer,Map<Integer,Set<Integer>>> index;
	private int nbTriple;
	
	public POSIndex(Dictionary dico, ArrayList<ArrayList<String>> triples){
		
		assert(triples.size() == 3);
		assert(triples.get(0).size()==triples.get(1).size());
		assert(triples.get(0).size()==triples.get(2).size());
		index = new HashMap<>();
		nbTriple = 0;
		
		for(int i=0;i<triples.get(0).size();i++) {
			Integer subj = dico.getIntegerId(triples.get(0).get(i));
			Integer pred = dico.getIntegerId(triples.get(1).get(i));
			Integer obj = dico.getIntegerId(triples.get(2).get(i));
			index.putIfAbsent(pred, new HashMap<>());
			Map<Integer,Set<Integer>> predMap = index.get(pred);
			predMap.putIfAbsent(obj, new HashSet<>());
			predMap.get(obj).add(subj);
			nbTriple++;
		}
		setLength();
	}

	@Override
	public Map<Integer,Map<Integer, Set<Integer>>> getDatas() {
		return index;
	}


	@Override
	public int nbTriple() {
		return nbTriple;
	}
		

}
