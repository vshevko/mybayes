package sneroll.myBayes.learning;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.math3.util.BigReal;

import sneroll.myBayes.BayesianNetwork;
import sneroll.myBayes.CPTInfo;
import sneroll.myBayes.CPTKey;
import sneroll.myBayes.ConditionalProbabilityTable;
import sneroll.myBayes.Node;

public class BayesianEstimation extends MaximumLikelihoodEstimation {

	public BayesianEstimation(BayesianNetwork bn, Collection<Map<String, Object>> allData) {
		super(bn, allData);
	}


	@Override
	public void process() {
		super.process();
		
		for (Node node : bn.getNodes()) {
			ConditionalProbabilityTable cpt = getNodeCPT(node);

			for (CPTKey key : cpt.getTable().keySet()) {
	
				CPTInfo info = cpt.getCPTInfo(key);
				
				for (Object value : node.getPosibleValues()) {
					// assume one example for each posible value of the node
					info.addToExpectedNumer(value, BigReal.ONE);
				}
			}
		}

	}
	
}
