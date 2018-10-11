package Dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.trie.PatriciaTrie;

public class PatriciaTrieDictionary implements Dictionnary {

	private PatriciaTrie<Integer> uriToIds;
	private ArrayList<String> idToUris;
	
	public PatriciaTrieDictionary(Collection<String> datas) {
		uriToIds = new PatriciaTrie<>();
		idToUris = new ArrayList<>(datas.size());
		
		for(String uri : datas){
			uriToIds.put(uri, 0);
		}
		
		int i=0;
		MapIterator<String,Integer> it = uriToIds.mapIterator();
		while(it.hasNext()) {					
			idToUris.add(it.next());
			it.setValue(i++); 
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