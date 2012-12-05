package sneroll.myBayes.learning;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.math3.util.BigReal;

import sneroll.myBayes.BayesUtils;
import sneroll.myBayes.BayesianNetwork;
import sneroll.myBayes.CPTInfo;
import sneroll.myBayes.CPTKey;
import sneroll.myBayes.ConditionalProbabilityTable;
import sneroll.myBayes.Node;

public class MaximumLikelihoodEstimation extends ParameterEstimation{


	public MaximumLikelihoodEstimation(BayesianNetwork bn, Collection<Map<String, Object>> allData) {
		super(bn, allData);
	}

	public void process() {

		for (Map<String, Object> data : allData) {
			
			System.out.println(data);
			
			for (Node node : bn.getNodes()) {
				ConditionalProbabilityTable cpt = getNodeCPT(node);

				CPTKey key = BayesUtils.getKey(node.getParents(), data);

				CPTInfo info = cpt.getCPTInfo(key);

				Object value = data.get(node.getName());

				node.addPosibleValue(value);
				info.addToExpectedNumer(value, BigReal.ONE);
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
	
	public Map<Node, ConditionalProbabilityTable> getCpts() {
		return cpts;
	}
	
}
