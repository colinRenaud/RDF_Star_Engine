package Index;

import java.util.Map;
import java.util.Set;

public abstract class Index {
	
	abstract Map<Integer,Map<Integer,Set<Integer>>> getDatas();
	
	public abstract int nbTriple();
	
	public Set<Integer> get(Integer key1, Integer key2){
		Map<Integer,Map<Integer,Set<Integer>>> datas = this.getDatas();
		return datas.get(key1).get(key2);
	}
	
	
		
	public static void displayDatas(Index index){
		Map<Integer,Map<Integer,Set<Integer>>> datas = index.getDatas();
		for(Integer k : datas.keySet()){
			Map<Integer,Set<Integer>> v = datas.get(k);
			for(Integer k2 : v.keySet()){
				Set<Integer> v2 = v.get(k2);
				for(Integer k3 : v2){
					System.out.println(k+":"+k2+":"+k3);
				}
			}
		}			
	}

}
