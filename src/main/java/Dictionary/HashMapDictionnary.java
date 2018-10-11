package Dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

public class HashMapDictionnary implements Dictionnary{
	
	private TreeMap<String,Integer> uriToIds;
	private ArrayList<String> idToUris;
	
	public HashMapDictionnary(Collection<String> datas) {
		uriToIds = new TreeMap<>();
		idToUris = new ArrayList<>(datas.size());
		
		for(String uri : datas){
			uriToIds.put(uri, 0);
		}
		
		int i=0;
		for(String key : uriToIds.keySet()){
			uriToIds.put(key,i++);	
			idToUris.add(key);
		}
	
	}

	@Override
	public Integer getIntegerId(String id) {
		return uriToIds.get(id);
	}

	@Override
	public String getStringId(Integer id) {
		return idToUris.get(id);
	}

	@Override
	public List<String> getURIs() {
		return idToUris;
	}

	@Override
	public Collection<Integer> getIds() {
		return uriToIds.values();
	}
	
}
