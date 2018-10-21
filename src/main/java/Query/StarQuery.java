package Query;

import java.util.ArrayList;

public class StarQuery {
	
	private ArrayList<Integer> predicatesIds;
	private ArrayList<Integer> objectsIds;
	private ArrayList<String> predicates;
	private ArrayList<String> objects;
	
	
	public StarQuery(ArrayList<Integer> predicatesIds, ArrayList<Integer> objectsIds, ArrayList<String> predicates,
			ArrayList<String> objects) {
		super();
		this.predicatesIds = predicatesIds;
		this.objectsIds = objectsIds;
		this.predicates = predicates;
		this.objects = objects;
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
		return "[predicates=" + predicatesIds.toString() + ", objects=" + objectsIds.toString() + "]";
	}
	
	
	
	
}
