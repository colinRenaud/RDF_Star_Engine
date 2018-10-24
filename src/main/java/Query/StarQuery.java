package Query;

import java.util.ArrayList;
import java.util.HashMap;

import Dictionary.Dictionary;

public class StarQuery {
	
	private ArrayList<Integer> predicatesIds;
	private ArrayList<Integer> objectsIds;
	private ArrayList<String> predicates;
	private ArrayList<String> objects;
	private final static String subjectSymbol = "s";
	
	public StarQuery(ArrayList<String> predicates, ArrayList<String> objects, Dictionary dico) {
		assert(! predicates.isEmpty());
		assert(predicates.size() == objects.size());
		
		predicatesIds = new ArrayList<>(predicates.size());
		objectsIds = new ArrayList<>(objects.size());
		this.predicates = new ArrayList<>(predicates);
		this.objects = new ArrayList<>(objects);
		
		
		for(int i=0;i<predicates.size();i++) {
			Integer predId = dico.getIntegerId(predicates.get(i));					
			Integer objId = dico.getIntegerId(objects.get(i));
			if(predId != null &&  objId != null) {
				predicatesIds.add(predId);
				objectsIds.add(objId);
			}
		}
	}
	
	public ArrayList<Integer> getPredicatesIds() {
		return predicatesIds;
	}
	
	public ArrayList<Integer> getObjectsIds() {
		return objectsIds;
	}

	
	
	public ArrayList<String> getPredicates() {
		return predicates;
	}

	public ArrayList<String> getObjects() {
		return objects;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder("SELECT ?s WHERE {\n");
		for(int i=0;i<predicates.size();i++) {
			sb.append("\t?"+subjectSymbol+" <"+predicates.get(i) +"> <"+objects.get(i) +"> .\n ");
		}
		sb.append("}");
		return sb.toString();
	}
	
	public String getSubjectSymbol() {
		return subjectSymbol;
	}
	
}
