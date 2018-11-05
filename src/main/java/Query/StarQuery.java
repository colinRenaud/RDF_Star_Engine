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
	private String name;
	
	public StarQuery(ArrayList<String> predicates, ArrayList<String> objects, Dictionary dico) throws IllegalArgumentException{
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
			else {
				throw new IllegalArgumentException("Unknown predId or objId");
			}
		}
		setName();
	}
	
	private void setName() {
		StringBuilder sb=new StringBuilder("SELECT ?s WHERE {\n");
		for(int i=0;i<predicates.size();i++) {
			sb.append("\t?"+subjectSymbol+" <"+predicates.get(i) +"> <"+objects.get(i) +"> .\n ");
		}
		sb.append("}");
		name = sb.toString();
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
		return name;
	}
	
	public String toOutputString() {
		StringBuilder sb=new StringBuilder("SELECT ?s WHERE {");
		for(int i=0;i<predicates.size();i++) {
			sb.append("?"+subjectSymbol+" <"+predicates.get(i) +"> <"+objects.get(i) +"> .");
		}
		sb.append("}");
		return sb.toString();
	}
	
	public String getSubjectSymbol() {
		return subjectSymbol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StarQuery other = (StarQuery) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
}
