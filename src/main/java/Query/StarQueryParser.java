package Query;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import Dictionary.Dictionary;

public class StarQueryParser {
	
	private Dictionary dico;
	
	
	
	
	public StarQueryParser(Dictionary dico) {
		super();
		this.dico = dico;
	}


	public List<StarQuery> readQueries(String path) throws IOException{ 
		
		List<String> lines = Files.readAllLines(Paths.get(path));			
		ArrayList<String> properties = new ArrayList<>(), objects = new ArrayList<>();
		List<StarQuery> queries = new LinkedList<>();
		
  		for(String st: lines){
  			if((st.isEmpty() || st.contains("{")) && ! properties.isEmpty()) { // new SPARQL query
  				queries.add( buildQuery(properties, objects));
  				properties.clear(); objects.clear();
  			}
  			else if(st.contains(">")) { // Need a fast checking bc of \t
  				st = st.replaceAll("<","");
  				st = st.replaceAll(">","");
				String[] parts = st.split(" ");
				properties.add(parts[1]);
				objects.add(parts[2]);				
  			}
  		}
  		return queries;
	}
	

	
	public StarQuery buildQuery(ArrayList<String> predicates, ArrayList<String> objects) {
		assert( ! predicates.isEmpty());
		assert(predicates.size() == objects.size());
		ArrayList<Integer> predicatesIds = new ArrayList<>(predicates.size());
		ArrayList<Integer> objectsIds = new ArrayList<>(objects.size());
		
		for(int i=0;i<predicates.size();i++) {
			Integer predId = dico.getIntegerId(predicates.get(i));		
			if(predId == null) {
				return null;
			}
			Integer objId = dico.getIntegerId(objects.get(i));
			if(objId == null) {
				return null;
			}
			predicatesIds.add(predId);
			objectsIds.add(objId);
		}
		StarQuery starQuery = new StarQuery(predicatesIds, objectsIds,predicates,objects);
		return starQuery;
	}
	
	
}
