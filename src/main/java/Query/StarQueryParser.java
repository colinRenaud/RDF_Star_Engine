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
		ArrayList<String> predicates = new ArrayList<>(), objects = new ArrayList<>();
		List<StarQuery> queries = new LinkedList<>();
		
  		for(String st: lines){
  			if(st.isEmpty() || st.contains("{") ) {
  				if(! predicates.isEmpty()) { // new SPARQL query
  					StarQuery query = new StarQuery(predicates, objects, dico);
  					queries.add(query);
  	  				predicates.clear(); objects.clear();
  				}
  			}
  			else if(st.contains(">")) { // Need a fast checking bc of \t
  				st = st.replaceAll("<","");
  				st = st.replaceAll(">","");
				String[] parts = st.split(" ");
				predicates.add(parts[1]);
				objects.add(parts[2]);				
  			}
  		}
  		if(! predicates.isEmpty()) { // new SPARQL query
				StarQuery query = new StarQuery(predicates, objects, dico);
				queries.add(query);
				predicates.clear(); objects.clear();
			}
  		return queries;
	}
		
}
