package Index;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import Dictionary.Dictionary;

public class Index {
	
	protected Set<Integer> predicates,objects,subjects;
	
	public Index(Dictionary dico,ArrayList<ArrayList<String>> triples) {
		
		assert(triples.size() == 3);
		assert(triples.get(0).size()==triples.get(1).size());
		assert(triples.get(0).size()==triples.get(2).size());
		
		predicates = new HashSet<>();
		objects = new HashSet<>();
		subjects = new HashSet<>();
			
		for(String subject : triples.get(0)) {
			Integer subjId = dico.getIntegerId(subject);
			subjects.add(subjId);
		}
		
		for(String predicate: triples.get(1)) {
			Integer predId = dico.getIntegerId(predicate);
			predicates.add(predId);
		}
		
		for(String object : triples.get(2)) {
			Integer objId = dico.getIntegerId(object);
			objects.add(objId);
		}
		
	}
	
}
