package Index;

import java.util.TreeMap;
import java.util.TreeSet;

public interface Index {
	
	TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> getDatas();
	
	int nbTriple();
	
	public default TreeSet<Integer> get(Integer key1, Integer key2){
		TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> datas = this.getDatas();
		return datas.get(key1).get(key2);
	}
		
	public static void displayDatas(Index index){
		TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> datas = index.getDatas();
		for(Integer k : datas.keySet()){
			TreeMap<Integer,TreeSet<Integer>> v = datas.get(k);
			for(Integer k2 : v.keySet()){
				TreeSet<Integer> v2 = v.get(k2);
				for(Integer k3 : v2){
					System.out.println(k+":"+k2+":"+k3);
				}
			}
		}
			
	}

}
