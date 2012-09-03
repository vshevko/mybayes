package sneroll.myBayes.learning;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.util.BigReal;

import sneroll.myBayes.BayesUtils;
import sneroll.myBayes.BayesianNetwork;
import sneroll.myBayes.CPTInfo;
import sneroll.myBayes.CPTKey;
import sneroll.myBayes.ConditionalProbabilityTable;
import sneroll.myBayes.Node;

public class MaximumLikelihoodEstimation {


	private BayesianNetwork bn;
	private Collection<Map<String, Object>> allData;
	private Map<Node, ConditionalProbabilityTable> cpts;
	
	public MaximumLikelihoodEstimation(
			BayesianNetwork bn,
			Collection<Map<String, Object>> allData) {
		this(bn, allData, new HashMap<Node, ConditionalProbabilityTable>());
	}

	public MaximumLikelihoodEstimation(
			BayesianNetwork bn,
			Collection<Map<String, Object>> allData,
			Map<Node, ConditionalProbabilityTable> cpts) {
		this.bn = bn;
		this.allData = allData;
		this.cpts = cpts;
	}

	public void solve() {

		for (Map<String, Object> data : allData) {
			for (Node node : bn.getNodes()) {
				ConditionalProbabilityTable cpt = getNodeCPT(node);

				CPTKey key = BayesUtils.getKey(node.getParents(), data);

				CPTInfo info = cpt.getCPTInfo(key);

				Object value = data.get(node.getName());

				node.putPosibleValue(value);
				info.addToDenominator(new BigReal(1));
				info.addToNumerator(value, new BigReal(1));
			}
		}
	}

	public ConditionalProbabilityTable getNodeCPT(Node node) {
		ConditionalProbabilityTable cpt = cpts.get(node);
		if (cpt == null) {
			cpt = new ConditionalProbabilityTable(node);
			cpts.put(node, cpt);
		}
		return cpt;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (ConditionalProbabilityTable cpt : cpts.values()) {
			sb.append("\n***********************\n");
			sb.append("CPT for Node ").append(cpt.getNode().getName()).append("\n");
			sb.append("\t Parents: ");
			for (Node parent : cpt.getNode().getParents()) {
				sb.append(" - ").append(parent.getName());
			}
			sb.append("\n\n");
			sb.append("\t");
			for (CPTKey key : cpt.getTable().keySet()) {
				sb.append("\t").append(key);
			}
			for (Object pv : cpt.getNode().getPosibleValues()) {
				sb.append("\n").append(pv);
				for (CPTKey key : cpt.getTable().keySet()) {
					sb.append("\t").append(cpt.getCPTInfo(key).getP(pv).doubleValue());
				}
			}
			
		}
		
		return sb.toString();
	}

}
