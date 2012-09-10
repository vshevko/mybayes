package sneroll.myBayes.learning;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.math3.util.BigReal;

import sneroll.myBayes.BayesUtils;
import sneroll.myBayes.BayesianNetwork;
import sneroll.myBayes.CPTInfo;
import sneroll.myBayes.CPTKey;
import sneroll.myBayes.ConditionalProbabilityTable;
import sneroll.myBayes.Node;


public class ExpectationMaximization extends ParameterEstimation {

	public static final int DEFAULT_ITERATIONS = 100;
	
	private int iterations;
	public ExpectationMaximization(BayesianNetwork bn, Collection<Map<String, Object>> allData) {
		this(bn, allData, DEFAULT_ITERATIONS);
	}
	
	public ExpectationMaximization(BayesianNetwork bn, Collection<Map<String, Object>> allData, int iterations) {
		super(bn, allData);
		this.iterations = iterations;
	}
	
	@Override
	public void process() {
		init();
		
		boolean convergence = false;
		for (int i = 0; i < iterations && !convergence; i ++) {
			expectationMaximization();
			// TODO check convergence
		}
	}
	
	void init() {
		Random ran = new Random(System.currentTimeMillis());
		cpts = new HashMap<Node, ConditionalProbabilityTable>();
		
		for (Node node : bn.getNodes()) {

			Set<CPTKey> allKeys = BayesUtils.getAllKeys(node);
			
			ConditionalProbabilityTable cpt = getNodeCPT(cpts, node);
			
			for (CPTKey key : allKeys) {
				
				SortedSet<BigReal> randomInitialProbs = new TreeSet<BigReal>();
				for (int i = 0; i < node.getPosibleValues().size() - 1; i ++) {
					randomInitialProbs.add(new BigReal(ran.nextDouble()));
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
	
	public void expectationMaximization() {
		Map<Node, ConditionalProbabilityTable> newParameters = new HashMap<Node, ConditionalProbabilityTable>();
		
		for (Map<String, Object> data : allData) {
			for (Node node : bn.getNodes()) {
				
				ConditionalProbabilityTable newCpt = getNodeCPT(newParameters, node);
				
				boolean completeEvidence = isCompleteEvidence(data, node);
				
				if (!completeEvidence) {

					Set<Map<String, Object>> posibilities = BayesUtils.getPosibleExamples(bn.getNodes(), data);
					
					Map<Map<String, Object>, BigReal> exampleWeights = new HashMap<Map<String, Object>, BigReal>();
					BigReal normalizer = BigReal.ZERO;
					for (Map<String, Object> posibleData : posibilities) {
					
						BigReal exampleWeight = evaluateNetwork(posibleData);
						exampleWeights.put(posibleData, exampleWeight);
						normalizer = normalizer.add(exampleWeight);
					}
		
					for (Map<String, Object> posibleData : posibilities) {
						CPTKey key = BayesUtils.getKey(node.getParents(), posibleData);

						CPTInfo info = newCpt.getCPTInfo(key);

						Object value = posibleData.get(node.getName());
						BigReal exampleWeight = exampleWeights.get(posibleData).divide(normalizer);

						info.addToExpectedNumer(value, exampleWeight);
					}
					
				} else {

					CPTKey key = BayesUtils.getKey(node.getParents(), data);

					CPTInfo info = newCpt.getCPTInfo(key);

					Object value = data.get(node.getName());

					node.putPosibleValue(value);
					info.addToExpectedNumer(value, BigReal.ONE);
				}
			}
		}
		cpts = newParameters;
	}

	private boolean isCompleteEvidence(Map<String, Object> data, Node node) {
		boolean completeEvidence = true;
		Set<Node> nondes = bn.getNodes();
		for (Node joinNode : nondes) {
			if (data.get(joinNode.getName()) == null) {
				completeEvidence = false;
			}
		}
		return completeEvidence;
	}

	public Map<Node, ConditionalProbabilityTable> getCurrentParameters() {
		return cpts;
	}
	
	public void setCurrentParameters(Map<Node, ConditionalProbabilityTable> currentParameters) {
		this.cpts = currentParameters;
	}
	
}
