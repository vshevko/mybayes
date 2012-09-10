package sneroll.myBayes;

import java.util.Set;
import java.util.TreeSet;

public class BayesianNetwork {

	
	private Set<Node> nodes  = new TreeSet<Node>();

	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public void addEdge(Node parent, Node child) {
		parent.addChild(child);
		child.addParent(parent);
		
		addNode(parent);
		addNode(child);
	}
	
	public Set<Node> getNodes() {
		return nodes;
	}

	
	public Set<Node> getNotDSeparatedNodes(Node node) {
		//TODO fix DSeparation
		return nodes;
	}
	
}
