package sneroll.myBayes;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class BayesianNetwork {

	
	private Set<Node> nodes  = new TreeSet<Node>();
	private DAGTester dagTester;

	public void addNode(Node node) {
		assert(dagTester == null);
		
		nodes.add(node);
	}
	
	public void addEdge(Node parent, Node child) {
		assert(dagTester == null);
		
		parent.addChild(child);
		child.addParent(parent);
		
		addNode(parent);
		addNode(child);
	}
	
	public Set<Node> getNodes() {
		return Collections.unmodifiableSet(nodes);
	}

	public boolean isDAG() {
		if (dagTester == null)
			dagTester = new DAGTester(this);
		
		return dagTester.isDAG();
	}
	
}
