package Query;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Dictionary.Dictionary;
import Index.*;


public class Query {

	public ArrayList<Set<String>> subject = new ArrayList<Set<String>>();
	public ArrayList<String> property = new ArrayList<>();
	public ArrayList<String> object = new ArrayList<>();
	
	public ArrayList<Integer> propertyIds = new ArrayList<>();
	public ArrayList<Integer> objectIds = new ArrayList<>();
	//Set<String> mySet = new HashSet<String>();
	public ArrayList<Set<Integer>> subjectIds = new ArrayList<Set<Integer>>();
	
	Dictionary dico;
	Index index;
	
	public Query(String path, Dictionary dico, Index index, String displayChoice) {
		this.dico = dico;
		this.index = index;
		
		queryMaking(path);
		querying(index);
		switch(displayChoice) {
			case("terminal"):
				displayResults();
				break;
			case("file"):
				fileOutputResults();
				break;
			case("both"):
				displayResults();
				fileOutputResults();
				break;
		}
	}
	
	public void queryMaking(String path) {
		
		fileReader(path);
		queryDictionnaryMatching();
		
	}
	
	public void querying(Index index) {
		
		for(int cpt = 1; cpt <= propertyIds.size(); cpt++) {
			for(Integer t : index.get(propertyIds.get(cpt), objectIds.get(cpt))) { System.out.println("\n" + t); }
			subjectIds.add(index.get(propertyIds.get(cpt), objectIds.get(cpt)));
		}
		unscramble();
		
	}
	
	public void fileReader(String path) {
		
		try {
			File file = new File(path); 
			List<String> lines = Files.readAllLines(Paths.get(path));	
//  		BufferedReader br = new BufferedReader(new FileInputStream(file)); 
//  		String st; 
	  		for(String st: lines){
	  			if(st.contains(">")) { // Need a fast checking bc of \t
	  				st = st.replaceAll("<","");
	  				st = st.replaceAll(">","");
					String[] parts = st.split(" ");
					System.out.print(parts[1]);
					System.out.print(parts[2]);
					property.add(parts[1]);
					object.add(parts[2]);
	  			}
	  		}
		}
		catch (IOException e) 
    		{ 
			e.printStackTrace(); 
    		} 
		
//  		while ((st = br.readLine()) != null) 
//		{
//			
//		}
	}
	
	public void queryDictionnaryMatching() {
		
		for(String prop : property) 
			propertyIds.add(dico.getIntegerId(prop));
		for(String obj : object) 		
			objectIds.add(dico.getIntegerId(obj));
					
	}
	
	public void unscramble() {
		
		int cpt = 0;
		for(Set<Integer> subjs : subjectIds) {
			for(Integer s : subjs) {
			System.out.println(s);
			subject.get(cpt).add(dico.getStringId(s)); }
		}
		cpt++;
	}
	
	public void displayResults() {
		
		for(int cpt = 0; cpt <= propertyIds.size(); cpt++) {
			
			System.out.println("\u001B[31mRequest n°"+cpt+"\u001B[0m : ?x0 "+property.get(cpt)+" "+object.get(cpt));
			System.out.println("\u001B[32mResult)\u001B[0m :" + subject.get(cpt));
				
		}
	}
	
	public void fileOutputResults() {
		
		try{
		
			File resultFile = new File("../../../../results/resultat.txt");
			resultFile.createNewFile();
			FileWriter resultFileWriter=new FileWriter(resultFile);
			for(int cpt = 0; cpt <= propertyIds.size(); cpt++) {
			
				resultFileWriter.write("\u001B[31mRequest n°"+cpt+"\u001B[0m : ?x0 "+property.get(cpt)+" "+object.get(cpt));
				resultFileWriter.write("\u001B[32mResult)\u001B[0m :" + subject.get(cpt)+"\n\n");
				
			}
			
			resultFileWriter.close();
		
		} 
		catch (Exception e) {}
		
	}
	
}
