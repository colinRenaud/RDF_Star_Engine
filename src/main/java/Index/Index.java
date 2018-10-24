package Index;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

public abstract class Index {
	
	private long length;
	
	abstract Map<Integer,Map<Integer,Set<Integer>>> getDatas();
	
	public abstract int nbTriple();
	
	public Set<Integer> get(Integer key1, Integer key2){
		Map<Integer,Map<Integer,Set<Integer>>> datas = this.getDatas();
		return datas.get(key1).get(key2);
	}
	
	
		
	public static void displayDatas(Index index,OutputStream out) throws IOException{
		StringBuilder sb = new StringBuilder();
		Map<Integer,Map<Integer,Set<Integer>>> datas = index.getDatas();
		for(Integer k : datas.keySet()){
			
			Map<Integer,Set<Integer>> v = datas.get(k);
			for(Integer k2 : v.keySet()){
				Set<Integer> v2 = v.get(k2);
				for(Integer k3 : v2){
					sb.append(k+":"+k2+":"+k3+"\n");					
				}
			}
		}	
		out.write(sb.toString().getBytes());
	}
	
	protected void setLength() {
		length = 0;
		Map<Integer,Map<Integer,Set<Integer>>> datas = getDatas();
		for(Integer i : datas.keySet()) {
			for(Integer j : datas.get(i).keySet()) {
				Set<Integer> values = datas.get(i).get(j);
				length += values.size();
			}
		}
	}

	public long getLength() {
		return length;
	}
	
	
	

}
