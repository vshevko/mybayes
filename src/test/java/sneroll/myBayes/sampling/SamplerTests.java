package sneroll.myBayes.sampling;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import sneroll.myBayes.BayesUtils;
import sneroll.myBayes.BayesianNetwork;
import sneroll.myBayes.ConditionalProbabilityTable;
import sneroll.myBayes.Node;

public class SamplerTests {

	@Test
	public void test_gibbsSampler() {
		Node x1 = new Node("x1");
		x1.addPosibleValues(new String[]{"1", "2"});
		Node x2 = new Node("x2");
		x2.addPosibleValues(new String[]{"1", "2"});
		Node x3 = new Node("x3");
		x3.addPosibleValues(new String[]{"x3_1", "x3_2"});
		
		BayesianNetwork bn = new BayesianNetwork();
		bn.addEdge(x1, x3);
		bn.addEdge(x2, x3);
		
		Map<Node, ConditionalProbabilityTable> cpts = new HashMap<Node, ConditionalProbabilityTable>();
		
		double[][] pXParent = new double[][] {
				{0.3}, // 1
				{0.7}  // 2
		};
		
		cpts.put(x1, BayesUtils.buildCPTFromMatrix(x1, pXParent));
		cpts.put(x2, BayesUtils.buildCPTFromMatrix(x1, pXParent));
		
		
		double[][] pX3 = new double[][] {
		//x1/x2  1/1  1/2  2/1  2/2
				{0.1, 0.2, 0.3, 0.4}, // x3_1
				{0.2, 0.4, 0.3, 0.1}  // x3_2
		};
		
		cpts.put(x3, BayesUtils.buildCPTFromMatrix(x3, pX3));
		
		GibbsSampler sampler = new GibbsSampler(bn, cpts, new HashMap<Node, Object>(), new SampleProcessorAdapter() {
			
			int count = 0;
			@Override
			public void processSample(Map<String, Object> data) {
				System.out.println((count++)+" "+data);
			}
		});
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		sampler.initData(data);
		sampler.modifyData(x1, data);
		System.out.println();
		
		sampler.sample();
	}
	
}
