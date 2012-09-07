package sneroll.myBayes.learning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import sneroll.myBayes.BayesUtils;
import sneroll.myBayes.BayesianNetwork;
import sneroll.myBayes.Node;

public class LearningTest {

	
	@Test
	public void test_expectationMaximization_init() {
		
		Node x1 = new Node("x1");
		x1.putPosibleValues(new String[]{"1", "2"});
		Node x2 = new Node("x2");
		x2.putPosibleValues(new String[]{"1", "2"});
		Node x3 = new Node("x3");
		x3.putPosibleValues(new String[]{"1", "2"});
		
		BayesianNetwork bn = new BayesianNetwork();
		bn.addEdge(x1, x3);
		bn.addEdge(x2, x3);
		
		ExpectationMaximization em = new ExpectationMaximization(bn, null);
		em.init();
		
		System.out.println(em);
	
	}
	
	/**
	 * example from: http://www.cse.ust.hk/bnbook/pdf/l07.h.pdf page 4
	 */
	@Test
	public void test_em_missingData() {
		
		Node x1 = new Node("x1");
		x1.putPosibleValues(new String[]{"1", "2"});
		Node x2 = new Node("x2");
		x2.putPosibleValues(new String[]{"1", "2"});
		Node x3 = new Node("x3");
		x3.putPosibleValues(new String[]{"1", "2"});
		
		BayesianNetwork bn = new BayesianNetwork();
		bn.addEdge(x1, x3);
		bn.addEdge(x2, x3);
		
		Collection<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		
		data.add(buildExample( "1", "1", "1"));
		
		data.add(buildExample(null, "1", "2"));
		data.add(buildExample( "1",null,null));
		
		data.add(buildExample( "2", "1", "1"));
		data.add(buildExample( "2", "1", "1"));
		data.add(buildExample( "2", "1", "2"));
		
		data.add(buildExample( "2",null, "1"));
		data.add(buildExample(null, "2",null));
		
		
		ExpectationMaximization em = new ExpectationMaximization(bn, data);
		em.solve();
		
		System.out.println(BayesUtils.conditionalProbabilityTableToString(em.getCurrentParameters()));
	}
	
	@Test
	public void test_em_missingData_step() {
		
		Node x1 = new Node("x1");
		x1.putPosibleValues(new String[]{"1", "2"});
		Node x2 = new Node("x2");
		x2.putPosibleValues(new String[]{"1", "2"});
		Node x3 = new Node("x3");
		x3.putPosibleValues(new String[]{"1", "2"});
		
		BayesianNetwork bn = new BayesianNetwork();
		bn.addEdge(x1, x2);
		bn.addEdge(x2, x3);
		

		Collection<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		
		//					   x1   x2   x3
		data.add(buildExample( "1", "1", "2"));
		data.add(buildExample( "1", "1", "1"));
		data.add(buildExample( "1", "2", "2"));
		data.add(buildExample( "2", "1", "1"));
		data.add(buildExample( "2", "2", "1"));
		data.add(buildExample( "2", "2", "2"));
		
		MaximumLikelihoodEstimation mle = new MaximumLikelihoodEstimation(bn, data);
		mle.solve();
		
		System.out.println(mle);
		
		Collection<Map<String, Object>> incompleteData = new ArrayList<Map<String, Object>>();
		
		incompleteData.add(buildExample( "1", "1", "1"));
		incompleteData.add(buildExample( "2", "2", "2"));
		incompleteData.add(buildExample( "1",null, "1"));
		incompleteData.add(buildExample( "2",null, "2"));
		
		ExpectationMaximization em = new ExpectationMaximization(bn, incompleteData, 1);
		em.setCurrentParameters(mle.getCpts());
		em.expectationMaximization();
		
		System.out.println(em);
		System.out.println();
	}
	
	/**
	 * Example taken from: http://www.cse.ust.hk/bnbook/pdf/l06.h.pdf page 32
	 */
	@Test
	public void test_maximumLikelihood() {
		
		
		Node x1 = new Node("x1");
		Node x2 = new Node("x2");
		Node x3 = new Node("x3");
		
		BayesianNetwork bn = new BayesianNetwork();
		bn.addEdge(x1, x3);
		bn.addEdge(x2, x3);
		
		Collection<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		
		data.add(buildExample("1", "1", "1"));
		data.add(buildExample("1", "1", "2"));
		data.add(buildExample("1", "1", "2"));
		
		data.add(buildExample("1", "2", "2"));
		data.add(buildExample("1", "2", "2"));
		data.add(buildExample("1", "2", "2"));
		
		data.add(buildExample("2", "1", "1"));
		data.add(buildExample("2", "1", "1"));
		data.add(buildExample("2", "1", "1"));
		data.add(buildExample("2", "1", "2"));
		
		data.add(buildExample("2", "2", "1"));
		data.add(buildExample("2", "2", "1"));
		
		data.add(buildExample("2", "2", "2"));
		data.add(buildExample("2", "2", "2"));
		data.add(buildExample("2", "2", "2"));
		data.add(buildExample("2", "2", "2"));
		
		MaximumLikelihoodEstimation mle = new MaximumLikelihoodEstimation(bn, data);
		mle.solve();
		
		// TODO do asserts
		System.out.println(mle);
		
	}
	
	private Map<String, Object> buildExample(String x1, String x2, String x3) {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("x1", x1);
		obj.put("x2", x2);
		obj.put("x3", x3);
		return obj;
	}
	
}
