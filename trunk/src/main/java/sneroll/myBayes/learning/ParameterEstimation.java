package sneroll.myBayes.learning;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.util.BigReal;

import sneroll.myBayes.BayesUtils;
import sneroll.myBayes.BayesianNetwork;
import sneroll.myBayes.ConditionalProbabilityTable;
import sneroll.myBayes.Node;

public abstract class ParameterEstimation {

	protected BayesianNetwork bn;

	public abstract void solve();

	protected Collection<Map<String, Object>> allData;
	protected Map<Node, ConditionalProbabilityTable> cpts;

	public ParameterEstimation(
			BayesianNetwork bn,
			Collection<Map<String, Object>> allData) {
		this(bn, allData, new HashMap<Node, ConditionalProbabilityTable>());
	}

	public ParameterEstimation(
			BayesianNetwork bn,
			Collection<Map<String, Object>> allData,
			Map<Node, ConditionalProbabilityTable> cpts) {
		this.bn = bn;
		this.allData = allData;
		this.cpts = cpts;
	}

	
	public ConditionalProbabilityTable getNodeCPT(Map<Node, ConditionalProbabilityTable> params, Node node) {
		ConditionalProbabilityTable cpt = params.get(node);
		if (cpt == null) {
			cpt = new ConditionalProbabilityTable(node);
			params.put(node, cpt);
		}
		return cpt;
	}

	@Override
	public String toString() {
		return BayesUtils.conditionalProbabilityTableToString(cpts);
	}

	/**
	 * 
	 * TODO should have a cache. Many repeated calculations
	 * 
	 * @param data
	 * @return
	 */
	public BigReal evaluateNetwork(Map<String, Object> data) {
		BigReal p = BigReal.ONE;
		for (Node node : bn.getNodes()) {
			BigReal nodeP = BayesUtils.getP(cpts, node, data);
			p = p.multiply(nodeP);
		}
		return p;
	}

}