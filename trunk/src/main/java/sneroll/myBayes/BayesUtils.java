package sneroll.myBayes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BayesUtils {

	public static CPTKey getKey(Set<Node> parents, Map<String, Object> data) {
		
		List<Object> keyValues = new ArrayList<Object>(parents.size());
		for (Node parent : parents) {
			Object value = null;
			
			value = data.get(parent.getName());

			keyValues.add(value);
		}
		
		return new CPTKey(keyValues);
	}
	
	public static Set<CPTKey> getAllKeys(Node node) {
		Set<CPTKey> allKeys = null;
		if (node.getParents().isEmpty()) {
			allKeys = new HashSet<CPTKey>(1);
			allKeys.add(new CPTKey(Collections.emptyList()));
			return allKeys;
		}
		
		for (Node parent : node.getParents()) {
			
			if (allKeys == null) {
				allKeys = new HashSet<CPTKey>();
				for (Object parentPV : parent.getPosibleValues()) {
					List<Object> key = new ArrayList<Object>(1);
					key.add(parentPV);
					allKeys.add(new CPTKey(key));
				}
			
			} else {
				Set<CPTKey> oldKeys = allKeys;
				allKeys = new HashSet<CPTKey>();
				for (CPTKey oldKey : oldKeys) {
					for (Object parentPV : parent.getPosibleValues()) {
						List<Object> key = new ArrayList<Object>(oldKey.getKey().size()+1);
						key.addAll(oldKey.getKey());
						key.add(parentPV);
						allKeys.add(new CPTKey(key));
					}
				}
				
			}
		}
		
		return allKeys;
	}
	
}
