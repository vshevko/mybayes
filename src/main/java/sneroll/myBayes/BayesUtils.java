package sneroll.myBayes;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.util.BigReal;

public class BayesUtils {

	public static ConditionalProbabilityTable buildCPTFromMatrix(Node node, double[][] p) {
		
		assert(p.length == node.getPosibleValues().size());
		Set<CPTKey> parentKeys = getAllKeys(node);
		for (double[] row : p) {
			assert(row.length == parentKeys.size());
		}
		
		ConditionalProbabilityTable cpt = new ConditionalProbabilityTable(node);
		
		int col = 0;
		for (CPTKey key : parentKeys) {
			CPTInfo info = cpt.getCPTInfo(key);
			int row = 0;
			for (Object pv : node.getPosibleValues()) {
				info.setNumerator(pv, new BigReal(p[row][col]));
				row ++;
			}
			col ++;
		}
		
		return cpt;
	}
	
	public static CPTKey getKey(Set<Node> parents, Map<String, Object> data) {
		
		LinkedHashMap<Node, Object> keyValues = new LinkedHashMap<Node, Object>(parents.size());
		for (Node parent : parents) {
			Object value = null;
			
			value = data.get(parent.getName());

			keyValues.put(parent, value);
		}
		
		return new CPTKey(keyValues);
	}
	
	public static Set<CPTKey> getAllKeys(Node node) {
		
		return BayesUtils.getKeysWithMissingData(node, null);
	}
	
	public static Set<CPTKey> getKeysWithMissingData(Node node, Map<String, Object> data) {
		
		if (node ==null)
			System.out.println();
		
		Set<CPTKey> allKeys = null;
		if (node.getParents().isEmpty()) {
			allKeys = new LinkedHashSet<CPTKey>(1);
			allKeys.add(new CPTKey(new LinkedHashMap<Node, Object>()));
			return allKeys;
		}
		
		for (Node parent : node.getParents()) {
			
			Object parentDataValue = data == null? null : data.get(parent.getName());
			boolean parentMissingData = data == null || parentDataValue == null;
			
			if (allKeys == null) {
				allKeys = new LinkedHashSet<CPTKey>();
				if (parentMissingData) {
					for (Object parentPV : parent.getPosibleValues()) {
						LinkedHashMap<Node, Object> key = new LinkedHashMap<Node, Object>(1);
						key.put(parent, parentPV);
						allKeys.add(new CPTKey(key));
					}
				} else {
					LinkedHashMap<Node, Object> key = new LinkedHashMap<Node, Object>(1);
					key.put(parent, parentDataValue);
					allKeys.add(new CPTKey(key));
				}
			
			} else {
				Set<CPTKey> oldKeys = allKeys;
				allKeys = new LinkedHashSet<CPTKey>();
				for (CPTKey oldKey : oldKeys) {
					if (parentMissingData) {
						for (Object parentPV : parent.getPosibleValues()) {
							LinkedHashMap<Node, Object> key = new LinkedHashMap<Node, Object>(oldKey.getKey().size()+1);
							key.putAll(oldKey.getKey());
							key.put(parent, parentPV);
							allKeys.add(new CPTKey(key));
						}
					} else {
						LinkedHashMap<Node, Object> key = new LinkedHashMap<Node, Object>(oldKey.getKey().size()+1);
						key.putAll(oldKey.getKey());
						key.put(parent, parentDataValue);
						allKeys.add(new CPTKey(key));
					}
				}
				
			}
		}
		

		return allKeys;
	}

	public static Set<Map<String, Object>> getPosibleExamples(Set<Node> nodes, Map<String, Object> data) {
		
		Set<Map<String, Object>> allPosibleExamples = null;
		
		for (Node node : nodes) {
			
			if (allPosibleExamples == null) {
				allPosibleExamples = new LinkedHashSet<Map<String,Object>>();
				if (data.get(node.getName()) == null) {
					for (Object pv : node.getPosibleValues()) {
						Map<String, Object> example = new HashMap<String, Object>();
						example.put(node.getName(), pv);
						allPosibleExamples.add(example);
					}
				} else {
					Map<String, Object> example = new HashMap<String, Object>();
					example.put(node.getName(), data.get(node.getName()));
					allPosibleExamples.add(example);
				}
			
			} else {
				Set<Map<String, Object>> oldPosibleExamples = allPosibleExamples;
				allPosibleExamples = new LinkedHashSet<Map<String, Object>>();
				for (Map<String, Object> oldExample : oldPosibleExamples) {
					
					if (data.get(node.getName()) == null) {
						for (Object pv : node.getPosibleValues()) {
							Map<String, Object> example = new HashMap<String, Object>();
							example.putAll(oldExample);
							example.put(node.getName(), pv);
							allPosibleExamples.add(example);
						}
					} else {
						Map<String, Object> example = new HashMap<String, Object>();
						example.putAll(oldExample);
						example.put(node.getName(), data.get(node.getName()));
						allPosibleExamples.add(example);
					}
				}
				
			}
		}
		

		return allPosibleExamples;
	}
	
	public static String conditionalProbabilityTableToString(Map<Node, ConditionalProbabilityTable> cpts) {
		StringBuilder sb = new StringBuilder();
		
		DecimalFormat df = new DecimalFormat("#.#####");
		
		for (ConditionalProbabilityTable cpt : cpts.values()) {
			sb.append("\n***********************\n");
			sb.append("CPT for Node ").append(cpt.getNode().getName()).append("\n");
			sb.append("\t Parents: ");
			for (Node parent : cpt.getNode().getParents()) {
				sb.append(" / ").append(parent.getName());
			}
			sb.append("\n\n");
			for (CPTKey key : cpt.getTable().keySet()) {
				sb.append("\t").append(key);
			}
			for (Object pv : cpt.getNode().getPosibleValues()) {
				sb.append("\n").append(pv);
				for (CPTKey key : cpt.getTable().keySet()) {
					sb.append("\t").append(df.format(cpt.getCPTInfo(key).getP(pv).doubleValue()));
				}
			}
			
		}
		
		return sb.toString();
	}

	public static BigReal getP(Map<Node, ConditionalProbabilityTable> cpts, Node node, Map<String, Object> data) {
		
		ConditionalProbabilityTable cpt = cpts.get(node);
		CPTKey key = BayesUtils.getKey(node.getParents(), data);
		CPTInfo info = cpt.getCPTInfo(key);
		
		Object value = data.get(node.getName());
		
		return info.getP(value);
	}

	public static double getLogP(Map<Node, ConditionalProbabilityTable> cpts,
			Node node, Map<String, Object> data) {
		
		return Math.log(getP(cpts, node, data).doubleValue());
	}
	
	public static double getP(BayesianNetwork bn, Map<Node, ConditionalProbabilityTable> cpts, Map<String, Object> data) {
		return Math.exp(getLogP(bn, cpts, data));
	}
	
	public static double getLogP(BayesianNetwork bn, Map<Node, ConditionalProbabilityTable> cpts, Map<String, Object> data) {
		
		double logP = 0;
		for (Node node : bn.getNodes()) {
			logP += getLogP(cpts, node, data);
		}
		
		return logP;
	}
	
	
	

	
	
}
