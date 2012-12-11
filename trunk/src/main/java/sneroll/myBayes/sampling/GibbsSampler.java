package sneroll.myBayes.sampling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.commons.math3.util.BigReal;

import sneroll.myBayes.BayesUtils;
import sneroll.myBayes.BayesianNetwork;
import sneroll.myBayes.ConditionalProbabilityTable;
import sneroll.myBayes.Node;

public class GibbsSampler implements Sampler {

	
	
	private int iterations = 500000;
	private int samplingFreq = 100;
	private int burin = iterations/4;
	
	private BayesianNetwork bn;
	protected Map<Node, ConditionalProbabilityTable> cpts;
	private Map<Node, Object> evidence;
	private List<Node> unkowns;
	private SampleProcessor processor;
	
	public GibbsSampler(BayesianNetwork bn,
			Map<Node, ConditionalProbabilityTable> cpts,
			Map<Node, Object> evidence,
			SampleProcessor processor) {
		
		this.bn = bn;
		this.cpts = cpts;
		this.evidence = evidence;
		this.processor = processor;
		
		this.unkowns = new ArrayList<Node>();
		this.unkowns.addAll(this.bn.getNodes());
		this.unkowns.removeAll(evidence.keySet());
	}

	/* (non-Javadoc)
	 * @see sneroll.myBayes.sampling.Sampler#setIterations(int)
	 */
	public Sampler setIterations(int iterations) {
		this.iterations = iterations;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see sneroll.myBayes.sampling.Sampler#setBurin(int)
	 */
	public Sampler setBurin(int burin) {
		this.burin = burin;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see sneroll.myBayes.sampling.Sampler#setSamplingFreq(int)
	 */
	public Sampler setSamplingFreq(int samplingFreq) {
		this.samplingFreq = samplingFreq;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see sneroll.myBayes.sampling.Sampler#sample()
	 */
	public String call() {
		
		processor.init();
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		initData(data);
		
		int i = 0;
		while(i < iterations) {
			
			Collections.shuffle(unkowns);
			
			for (Node changedNode : unkowns) {
				modifyData(changedNode, data);
			}
			
			processor.processTempSample(data);
			
			if (i > burin && i % samplingFreq == 0) {
				processor.processSample(data);
			}
			i ++;
		}
		
		return processor.end();
	}


	void modifyData(Node unknown, Map<String, Object> data) {
			
			
		ArrayList<Double> v = new ArrayList<Double>(unknown.getPosibleValues().size());
		double normalizationFactor = 0;
		
		for (Object pv : unknown.getPosibleValues()) {
			data.put(unknown.getName(), pv);
			double mbP = evaluateMarkovBlanket(unknown, data);
			v.add(mbP);
			normalizationFactor += mbP;
		}
		
		double rand = random.nextDouble();
		double lastValue = 0;
		
		for (int i = 0; i < v.size(); i++) {
			double nextValue = lastValue + v.get(i)/normalizationFactor;
			
			if (lastValue < rand && rand < nextValue) {
				data.put(unknown.getName(), unknown.getPosibleValues().get(i));
				
				break;
			}
			lastValue = nextValue;
		}
	}

	void initData(Map<String, Object> data) {
		for (Entry<Node, Object> e: evidence.entrySet()) {
			data.put(e.getKey().getName(), e.getValue());
		}
		for (Node unknown : unkowns) {
			int index = random.nextInt(unknown.getPosibleValues().size());
			Object value = unknown.getPosibleValues().get(index);
			data.put(unknown.getName(), value);
		}
	}

	public double evaluateMarkovBlanket(Node baseNode, Map<String, Object> data) {
		double p = BayesUtils.getP(cpts, baseNode, data).doubleValue();
		for (Node node : baseNode.getChildren()) {
			BigReal nodeP = BayesUtils.getP(cpts, node, data);
			p *= nodeP.doubleValue();
		}
		return p;
	}
	
}
