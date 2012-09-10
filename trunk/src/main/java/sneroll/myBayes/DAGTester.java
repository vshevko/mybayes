package sneroll.myBayes;

import java.util.HashSet;
import java.util.Set;



class DAGTester {

	private final BayesianNetwork bn;
	private Boolean isDAG;
	
	public DAGTester(BayesianNetwork bn) {
		this.bn = bn;
	}
	
	boolean testDAG() {
		boolean isDAG = true;
		
		Set<Node> visitedNodes = new HashSet<Node>();
		
		for (Node node : bn.getNodes()) {
			if (desendantsContainsNode(node, node, visitedNodes)) {
				isDAG = false;
				break;
			}
		}
		
		return isDAG;
	}
	
	private boolean desendantsContainsNode(Node node, Node toTest, Set<Node> visitedNodes) {

		if (visitedNodes.contains(node))
			return false;
		
		visitedNodes.add(node);
		
		if (node.getChildren().contains(toTest))
			return true;
		
		for (Node child : node.getChildren())
			return desendantsContainsNode(child, toTest, visitedNodes);
		
		return false;
	}

	boolean isDAG() {
		if (isDAG == null)
			isDAG = Boolean.valueOf(testDAG());
		
		return isDAG.booleanValue();
	}
	
}
