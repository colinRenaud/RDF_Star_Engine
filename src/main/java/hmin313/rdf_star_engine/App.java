package hmin313.rdf_star_engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;

import Query.StarQuery;
import Query.Result;
import Query.ResultSet;


public class App {
	
	
	public final static String 
		QUERY_TIMES_FILE = "exec_time.csv", 
		QUERY_STATS_FILE = "query_stats",
		QUERY_RESULTS_FILE = "query_results.csv";
	
	static String queryPath,dataPath,outputDir;
	static Boolean verbose,export_stats,export_results,workload_time;
	static File statFile,resultsFile,timeFile;

	
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
	
	public static List<File> createOutputFiles(String outputDir) throws IOException {
	   List<File> files = new LinkedList<>();
	   if(outputDir != null) {
		   files.add(new File(outputDir+QUERY_TIMES_FILE));
		   if(export_stats)
			   files.add(new File(outputDir+QUERY_STATS_FILE));
		   if(export_results)
			   files.add(new File(outputDir+QUERY_RESULTS_FILE));
	   }
   
	    for(File file : files) {
	    	if(file.exists()) 
	    		file.delete(); 	    	
	    	file.createNewFile();		    		    		
	    }
	    return files;
	}
	
	
	public static void exportResult(ResultSet queriesResults) throws IOException {
		
		 PatriciaTrie<Result> map = queriesResults.getResults();
		 FileWriter fw = new FileWriter(outputDir+QUERY_RESULTS_FILE,true);		
		 for(String queryName: map.keySet()) {
			 fw.write(queryName+"\n");
			 for(String rslt : map.get(queryName).getResults()) {
				 fw.write(rslt+"\n");
			 }
			 fw.write("\n");
		 }
		 fw.close();
	}
	
	
	public static void exportTimes(ResultSet queriesResults) throws IOException {
		
		 PatriciaTrie<Result> map = queriesResults.getResults();
		 FileWriter fw = new FileWriter(outputDir+QUERY_TIMES_FILE,true);
		 for(String query : map.keySet()) {
			 fw.write(query+","+map.get(query).getTime()+"\n");
		 }
		 fw.close();
	}
	
    public static void main( String[] args ){	
    	
    	if(args.length < 4) {
    		badUsage(args);
    	}
    	if(! args[0].equals("-queries") || ! args[2].equals("-data")) {
    		badUsage(args);
    	}
    	
    	
    	HashMap<String,Integer> options = new HashMap<>();   
    	for(int i=0;i<args.length;i++)
    		options.put(args[i],i);
    	
    	System.out.println("RDF_STAR_ENGINE [START] \n\t"+Arrays.asList(args));
    			
    	queryPath = args[1];
    	dataPath = args[3];
    	
    	Integer outputIdx = options.get("-output");
    	boolean output = (outputIdx != null && (outputIdx+1) < args.length);
    	
    	if(output) {
    		outputDir =args[outputIdx+1];
    		File outputDirFile = new File(outputDir);
    	    if(! outputDirFile.exists()) {
    	    	outputDirFile.mkdir();
    	    }
    	}
    	
        verbose = options.containsKey("-verbose");
        export_stats = options.containsKey("-export_stats");
        export_results = options.containsKey("-export_results");
        workload_time = options.containsKey("-workload_time");
        
        try {    
        	createOutputFiles(outputDir);
			RDF_StarEngine engine = new RDF_StarEngine(dataPath,verbose,workload_time,outputDir);		
			System.out.println("RDF_STAR_ENGINE BUILDING [OK]\n");
			
			Instant t1 = Instant.now();
			ResultSet results = engine.runQueryInDir(queryPath);
			
			if(workload_time) {
		    	System.out.println("\nWorkload evaluation time  : "+Duration.between(t1,Instant.now()).toMillis()+"ms");			}
			
			if(export_results) {
				exportResult(results);				
			}	
			if(export_stats) {
				
			}
			exportTimes(results);
			
			
		} catch (RDFParseException | RDFHandlerException | IOException e) {
			e.printStackTrace();
		}
      
       System.out.println("RDF_STAR_ENGINE [FINISH]");
    }
    
    public static void DisplayAndResetInstant(Instant t, String msg) {
    	
    }
    
    

    
    
}
