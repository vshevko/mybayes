package sneroll.myBayes;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CPTKey {

	private List<Object> key;
	
	public CPTKey(List<Object> key) {
		this.key = key;
	}
	
	public List<Object> getKey() {
		return key;
	}
	
	@Override
	public int hashCode() {
		
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(key.toArray());
		
		return hcb.build();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		CPTKey other = (CPTKey) obj;
		
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(this.key.toArray(), other.key.toArray());
		
		return eb.build();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if (this.key.isEmpty()) {
			sb.append("*");
		}
		
		boolean first = true;
		for(Object key : this.key) {
			if (!first)
				sb.append("/");
			sb.append(key);
			first = false;
		}
		
		return sb.toString();
	}
	
}
