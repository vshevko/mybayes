package sneroll.myBayes;

import java.util.Collections;
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
	public void test_bayesUtils_allKeys() {
		Node a = new Node("a");
		a.putPosibleValues(new String[]{"1", "2"});
		Node b = new Node("b");
		b.putPosibleValues(new String[]{"1", "2"});
		Node c = new Node("c");
		c.putPosibleValues(new String[]{"1", "2"});
		
		BayesianNetwork bn = new BayesianNetwork();
		bn.addEdge(a, b);
		bn.addEdge(c, b);
		
		
		Set<CPTKey> keys = BayesUtils.getAllKeys(b);
		
		Assert.assertEquals(a.getPosibleValues().size()*c.getPosibleValues().size(), keys.size());
		
	}
}
