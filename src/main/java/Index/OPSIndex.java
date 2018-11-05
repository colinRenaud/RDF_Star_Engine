package Index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import Dictionary.Dictionary;

public class OPSIndex extends Tri_Index{
	
	
	public OPSIndex(Dictionary dico, ArrayList<ArrayList<String>> triples){
		
		assert(triples.size() == 3);
		assert(triples.get(0).size()==triples.get(1).size());
		assert(triples.get(0).size()==triples.get(2).size());

		index = new HashMap<>();
		nbTriple = 0;
		for(int i=0;i<triples.get(0).size();i++) {
			Integer subj = dico.getIntegerId(triples.get(0).get(i));
			Integer pred = dico.getIntegerId(triples.get(1).get(i));
			Integer obj = dico.getIntegerId(triples.get(2).get(i));
			
			index.putIfAbsent(obj, new HashMap<>());
			Map<Integer,Set<Integer>> objMap = index.get(obj);
			objMap.putIfAbsent(pred, new HashSet<>());
			objMap.get(pred).add(subj);
			nbTriple++;
		}
		setLength();
	}
		

}
