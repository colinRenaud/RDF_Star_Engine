package hmin313.rdf_star_engine;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;


public class App {
	
	public static IllegalArgumentException badUsage(String[] args) {
		StringBuilder msg = new StringBuilder("Error bad usage : \n");
		for(String arg : args) {
			msg.append(arg+" ");
		}
		msg.append("\n\nOptions : \n"
				+ "-queries /chemin/vers/requetes\n" + 
				"-data  /chemin/vers/donnees\n" + 
				"-output /chemin/vers/dossier/sortie");
		throw new IllegalArgumentException(msg.toString());
	}
	
    public static void main( String[] args ){	
    	
    	if(args.length < 6) {
    		badUsage(args);
    	}
    	if(! args[0].equals("-queries") || ! args[2].equals("-data") || ! args[4].equals("-output")) {
    		badUsage(args);
    	}
    	Collection<String> options = new HashSet<>(Arrays.asList(args));     	
    	String queryPath = args[1];
    	String dataPath = args[3];
        String outputDir = args[5];       
        Boolean verbose = options.contains("-verbose");
        Boolean workload_time = options.contains("workload_time");
        Boolean export_stats = options.contains("export_stats");
        Boolean export_results = options.contains("export_results");
        
        try {
        	Instant t1 = Instant.now();
			RDF_StarEngine engine = new RDF_StarEngine(dataPath,verbose);
			if(verbose) {
				System.out.println("DataBase Building time:"+Duration.between(t1,Instant.now()).toMillis()+"ms [OK]");
			}
			
		} catch (RDFParseException | RDFHandlerException | IOException e) {
			e.printStackTrace();
		}
       
    }
    
    public static void DisplayAndResetInstant(Instant t, String msg) {
    	
    }
    
    

    
    
}
