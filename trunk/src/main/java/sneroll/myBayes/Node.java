package sneroll.myBayes;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Node implements Comparable<Node>{
	
	private Set<Object> posibleValues = new LinkedHashSet<Object>();
	private SortedSet<Node> parents;
	private SortedSet<Node> children;
	private final String name;
	
	public Node(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void putPosibleValue(Object value) {
		posibleValues.add(value);
	}
	
	public void putPosibleValues(Collection<Object> values) {
		posibleValues.addAll(values);
	}
	
	public void putPosibleValues(Object[] values) {
		for (Object value : values)
			putPosibleValue(value);
	}
	
	public void addParent(Node parent) {
		if (parents == null)
			parents = new TreeSet<Node>();
		
		parents.add(parent);
	}
	
	public void addChild(Node child) {
		if (children == null)
			children = new TreeSet<Node>();
		
		children.add(child);
	}
	
	public Set<Node> getParents() {
		if (parents == null)
			return Collections.emptySet();
		
		return Collections.unmodifiableSet(parents);
	}
	
	public Set<Node> getChildren() {
		if (children == null)
			return Collections.emptySet();
		
		return Collections.unmodifiableSet(children);
	}
	
	public Set<Object> getPosibleValues() {
		return Collections.unmodifiableSet(posibleValues);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Node [name=" + name + "]";
	}

	public int compareTo(Node o) {
		return this.name.compareTo(o.name);
	}

	
}
