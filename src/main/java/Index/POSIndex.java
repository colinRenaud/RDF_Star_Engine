package Index;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import Dictionary.Dictionary;

public class POSIndex implements Index{
	
	private TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> index;
	private int nbTriple;
	
	public POSIndex(Dictionary dico, ArrayList<ArrayList<String>> triples){
		index = new TreeMap<>();
		nbTriple = 0;
		
		for(ArrayList<String> triple: triples){
			Integer obj = dico.getIntegerId(triple.get(0));
			Integer pred = dico.getIntegerId(triple.get(1));
			Integer subj = dico.getIntegerId(triple.get(2));
			index.putIfAbsent(pred, new TreeMap<>());
			TreeMap<Integer,TreeSet<Integer>> predMap = index.get(pred);
			predMap.putIfAbsent(obj, new TreeSet<>());
			predMap.get(obj).add(subj);
			nbTriple++;
		}	
	}


	@Override
	public TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>> getDatas() {
		return index;
	}


	@Override
	public int nbTriple() {
		return nbTriple;
	}
		

}
