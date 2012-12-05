package sneroll.myBayes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

public class BayesianNetworkTest {

	@Test
	public void test_bayesianNetwork() {
		
		Node a = new Node("a");
		Node b = new Node("b");
		Node c = new Node("c");
		
		BayesianNetwork bn = new BayesianNetwork();
		bn.addEdge(a, b);
		bn.addEdge(c, b);
		
		Assert.assertEquals(a.getParents().size(), 0);
		Assert.assertEquals(b.getParents().size(), 2);
		Assert.assertEquals(c.getParents().size(), 0);
		
		Assert.assertEquals(a.getChildren().size(), 1);
		Assert.assertEquals(b.getChildren().size(), 0);
		Assert.assertEquals(c.getChildren().size(), 1);
	}
	
	@Test
	public void test_buildCPTFromMatrix() {
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
		
		System.out.println(BayesUtils.conditionalProbabilityTableToString(cpts));
		
	}
	
	@Test
	public void test_bayesUtils_allKeys() {
		Node x1 = new Node("x1");
		x1.addPosibleValues(new String[]{"1", "2"});
		Node x2 = new Node("x2");
		x2.addPosibleValues(new String[]{"1", "2"});
		Node x3 = new Node("x3");
		x3.addPosibleValues(new String[]{"1", "2"});
		
		BayesianNetwork bn = new BayesianNetwork();
		bn.addEdge(x1, x3);
		bn.addEdge(x2, x3);
		
		
		Set<CPTKey> keys = BayesUtils.getAllKeys(x3);
		Assert.assertEquals(x1.getPosibleValues().size()*x3.getPosibleValues().size(), keys.size());

		
		keys = BayesUtils.getKeysWithMissingData(x3, buildExample("1",null, "1"));
		Assert.assertEquals(x1.getPosibleValues().size(), keys.size());
		
		keys = BayesUtils.getKeysWithMissingData(x3, buildExample(null,null, "1s"));
		Assert.assertEquals(x1.getPosibleValues().size()*x3.getPosibleValues().size(), keys.size());
	}
	
	private Map<String, Object> buildExample(String x1, String x2, String x3) {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("x1", x1);
		obj.put("x2", x2);
		obj.put("x3", x3);
		return obj;
	}
	
	@Test
	public void test_DAGTester() {
		Node x1 = new Node("x1");
		Node x2 = new Node("x2");
		Node x3 = new Node("x3");
		
		BayesianNetwork bn = new BayesianNetwork();
		bn.addEdge(x1, x3);
		bn.addEdge(x2, x3);
		
		Assert.assertEquals(true, bn.isDAG());
		
		bn = new BayesianNetwork();
		bn.addEdge(x1, x2);
		bn.addEdge(x2, x3);
		bn.addEdge(x3, x1);
		
		Assert.assertEquals(false, bn.isDAG());
	}
}
