import java.util.ArrayList;
import dictionnaire.Dictionnaire;
import index.*;

public class Query {

	public ArrayList<String> subject;
	public ArrayList<String> property;
	public ArrayList<String> object;
	public ArrayList<int> propertyIds;
	public ArrayList<int> objectIds;
	public ArrayList<int> subjectIds;
	
	public void queryMain(String path, Dictionnaire dico, Index index, String displayChoice) {
		
		queryMaking(path, dico);
		querying(index, dico);
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
	
	public void queryMaking(String path, Dictionnaire dico) {
		
		fileReader(path);
		quieryDictionnaryMatching(dico);
		
	}
	
	public void querying(Index index, Dictionnaire Dico) {
		
		for(int cpt = 0; cpt <= propertyIds.size; cpt++) 
			subjectIds.add(index.get(propertyIds.get[cpt], objects.Ids[cpt]));
		
		unscramble(dico);
		
	}
	
	public void fileReader(String path) {
		
		try {
			File file = new File(path); 
		}
		catch (IOException e) 
    		{ 
			e.printStackTrace(); 
    		} 
			
  		BufferedReader br = new BufferedReader(new FileReader(file)); 
  		String st; 
  		while ((st = br.readLine()) != null) 
		{
			if(st.contains('>')) { // Need a fast checking bc of \t
    				String[] parts = st;
				subject.add(parts[0]);
				property.add(parts[1]);
				object.add(parts[2]);
			}
		}
	}
	
	public void queryDictionnaryMatch() {
		
		for(String prop : property) 
			propertyIds.add(dico.getIntegerId(prop));
		for(String subj : subject) 		
			subj.add(dico.getIntegerId(subj));
					
	}
	
	public void unscramble() {
		
		for(String subj : subjectIds) 
			subject.add(dico.getStringId(subj));
		
	}
	
	public void displayResults() {
		
		for(int cpt = 0; cpt <= propertyIds.size; cpt++) {
			
			System.out.println("\u001B[31mRequest n°"+cpt)+"\u001B[0m : ?x0 "+property.get(cpt)+" "+object.get(cpt);
			System.out.println("\u001B[32mResult)\u001B[0m :" + subject.get(cpt);
				
		}
	}
	
	public void fileOutputResults() {
		
		try{
		
			File resultFile = new File("../../../../results/resultat.txt");
			resultFile.createNewFile();
			FileWriter resultFileWriter=new FileWriter(resultFile);
			for(int cpt = 0; cpt <= propertyIds.size; cpt++) {
			
				resultFileWriter.writeSystem.out.println("\u001B[31mRequest n°"+cpt)+"\u001B[0m : ?x0 "+property.get(cpt)+" "+object.get(cpt);
				resultFileWriter.writeSystem.out.println("\u001B[32mResult)\u001B[0m :" + subject.get(cpt)+"\n\n;
				
			}
			
			resultFileWriter.close();
		
		} 
		catch (Exception e) {}
		
	}
	
}
