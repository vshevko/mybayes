package sneroll.myBayes;

import java.util.Set;
import java.util.TreeSet;

public class BayesianNetwork {

	
	private Set<Node> nodes  = new TreeSet<Node>();

	public void addEdge(Node parent, Node child) {
		parent.addChild(child);
		child.addParent(parent);
		
		nodes.add(parent);
		nodes.add(child);
	}
	
	public Set<Node> getNodes() {
		return nodes;
	}

	
	public Set<Node> getNotDSeparatedNodes(Node node) {
		//TODO fix DSeparation
		return nodes;
	}
	
}
