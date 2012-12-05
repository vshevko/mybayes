package sneroll.myBayes;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.map.LRUMap;

public class BayesianNetwork {

	private LRUMap markovBlankets;
	private TreeSet<Node> nodes  = new TreeSet<Node>();
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

	public Node getNode(String nodeName) {
		return nodes.floor(new Node(nodeName));
		
	}
	
//	public Set<Node> getMarkovBlanket(Node node) {
//		if (markovBlankets == null) {
//			// allow a cache size between 100 and 1000
//			int cacheSize = nodes.size() < 100 ? nodes.size() : 75 + nodes.size()/4;
//			cacheSize = cacheSize < 1000 ? cacheSize : 1000;
//			markovBlankets = new LRUMap(cacheSize);
//		}
//		
//		@SuppressWarnings("unchecked")
//		Set<Node> blanket = (Set<Node>) markovBlankets.get(node);
//		if (blanket == null) {
//			blanket = buildMarkovBlanket(node);
//			markovBlankets.put(node, blanket);
//		}
//		
//		return blanket;
//	}
//
//	private Set<Node> buildMarkovBlanket(Node node) {
//		Set<Node> blanket = new HashSet<Node>();
//		blanket.add(node);
//		blanket.addAll(node.getChildren());
//		
//		return Collections.unmodifiableSet(blanket);
//	}
	
}
