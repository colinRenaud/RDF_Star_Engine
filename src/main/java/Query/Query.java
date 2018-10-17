package Query;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Dictionary.Dictionary;
import Index.Index;


public class Query {

	public ArrayList<String> subject;
	public ArrayList<String> property;
	public ArrayList<String> object;
	
	public ArrayList<Integer> propertyIds;
	public ArrayList<Integer> objectIds;
	public ArrayList<Integer> subjectIds;
	
	Dictionary dico;
	
	public void queryMain(String path, Dictionary dico, Index index, String displayChoice) {
		this.dico = dico;
		
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
		
//		for(int cpt = 0; cpt <= propertyIds.size(); cpt++) 
//			subjectIds.add(index.get(propertyIds.get(cpt), objectIds.get(cpt));
		
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
					String[] parts = st.split(" ");
					subject.add(parts[0]);
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
		for(String subj : subject) 		
			subjectIds.add(dico.getIntegerId(subj));
					
	}
	
	public void unscramble() {
		
		for(Integer subj : subjectIds) 
			subject.add(dico.getStringId(subj));
		
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
