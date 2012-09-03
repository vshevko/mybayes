package sneroll.myBayes.learning;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.math3.util.BigReal;
import org.junit.internal.ExactComparisonCriteria;

import sneroll.myBayes.BayesUtils;
import sneroll.myBayes.BayesianNetwork;
import sneroll.myBayes.CPTInfo;
import sneroll.myBayes.CPTKey;
import sneroll.myBayes.ConditionalProbabilityTable;
import sneroll.myBayes.Node;


public class ExpectationMaximization {

	public static final int DEFAULT_ITERATIONS = 100;

	private int iterations;
	private BayesianNetwork bn;
	private Collection<Map<String, Object>> allData;
	private Collection<Map<String, Object>> expectedData;
	
	private MaximumLikelihoodEstimation currentParameters;
	
	public ExpectationMaximization(BayesianNetwork bn, Collection<Map<String, Object>> allData) {
		this(bn, allData, DEFAULT_ITERATIONS);
	}
	
	public ExpectationMaximization(BayesianNetwork bn, Collection<Map<String, Object>> allData, int iterations) {
		this.bn = bn;
		this.allData = allData;
		this.iterations = iterations;
	}
	
	public void solve() {
		init();
		
		boolean convergence = false;
		for (int i = 0; i < iterations && !convergence; i ++) {
			expectation();
			maximization();
			// TODO check convergence
			if (false) {
				convergence = true;
			}
		}
	}
	
	void init() {
		currentParameters = new MaximumLikelihoodEstimation(bn, allData);
		
		for (Node node : bn.getNodes()) {

			Set<CPTKey> allKeys = BayesUtils.getAllKeys(node);
			
			ConditionalProbabilityTable cpt = currentParameters.getNodeCPT(node);
			
			for (CPTKey key : allKeys) {
				
				SortedSet<BigReal> randomInitialProbs = new TreeSet<BigReal>();
				for (int i = 0; i < node.getPosibleValues().size() - 1; i ++) {
					randomInitialProbs.add(new BigReal(Math.random()));
				}
				randomInitialProbs.add(BigReal.ONE);
				
				Queue<BigReal> queue = new LinkedList<BigReal>();
				queue.addAll(randomInitialProbs);
				
				BigReal lastProb = BigReal.ZERO;
				for (Object pv : node.getPosibleValues()) {
					BigReal p = queue.poll();
					
					CPTInfo info = cpt.getCPTInfo(key);
					info.setNumerator(pv, p.subtract(lastProb));
					info.setDenominator(BigReal.ONE);
					lastProb = p;
				}
			}
		}
		
	}
	
	public MaximumLikelihoodEstimation getCurrentParameters() {
		return currentParameters;
	}
	
	void expectation() {
		
		expectedData = new LinkedList<Map<String, Object>>();
		for (Map<String, Object> d : allData) {
			Map<String, Object> expected = addExpectationData(d);
			expectedData.add(expected);
		}
	}

	Map<String, Object> addExpectationData(Map<String, Object> data) {
		
		
		return null;
	}

	private void maximization() {
		
	}
}
