package Dictionary;


import java.util.Collection;
import java.util.List;

public interface Dictionnary {
	
	public Integer getIntegerId(String id);
	
	public String getStringId(Integer id);
	
	public List<String> getURIs();
	
	public Collection<Integer> getIds();
	
	
}
