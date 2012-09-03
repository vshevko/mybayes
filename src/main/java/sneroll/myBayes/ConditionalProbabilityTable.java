package sneroll.myBayes;

import java.util.HashMap;


public class ConditionalProbabilityTable {

	private Node node;
	private HashMap<CPTKey, CPTInfo> table;
	
	public ConditionalProbabilityTable(Node node) {
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}
	
	public HashMap<CPTKey, CPTInfo> getTable() {
		return table;
	}
	
	public CPTInfo getCPTInfo(CPTKey key) {
		if (table == null)
			table = new HashMap<CPTKey, CPTInfo>();
		
		CPTInfo info = table.get(key);
		if (info == null) {
			info = new CPTInfo();
			table.put(key, info);
		}
		return info;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (CPTKey key : table.keySet()) {
			sb.append("\n").append(key).append(" = [ ").append(table.get(key)).append(" ]");
		}
		
		return sb.toString();
	}
	
}
