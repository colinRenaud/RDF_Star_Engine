package jena;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

import Query.StarQuery;

public class JenaQueryEngine {
	
	Model model;
	
	public JenaQueryEngine(String dataPath) {
		model = ModelFactory.createDefaultModel();
		Instant t1 = Instant.now();
		InputStream in = FileManager.get().open(dataPath);
		model.read(in, null);
		System.out.println("Jena Model building [OK] \n"
				+"\ttime="+Duration.between(t1,Instant.now()).toMillis()+"ms");
	}
	
	public ArrayList<String> getResults(StarQuery starQuery){
		
		ArrayList<String> results = new ArrayList<>();
		
		Query query = QueryFactory.create(starQuery.toString());		
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		ResultSet rs = qexec.execSelect();
		
		while(rs.hasNext()) {
			QuerySolution qs = rs.next();
			String value = qs.get(starQuery.getSubjectSymbol()).toString();
			results.add(value);
		}		
		return results;
	}

}
