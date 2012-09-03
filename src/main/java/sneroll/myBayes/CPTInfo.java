package sneroll.myBayes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.util.BigReal;

public class CPTInfo {

	private Map<Object, BigReal> numerator;
	private BigReal denominator;
	
	public Set<Object> getX() {
		return numerator.keySet();
	}
	
	public void setNumerator(Object possibleNodeValue, BigReal value) {
		if (numerator == null)
			numerator = new HashMap<Object, BigReal>();
		
		numerator.put(possibleNodeValue, value);
	}
	
	public void setDenominator(BigReal denominator) {
		this.denominator = denominator;
	}
	
	public void addToDenominator(BigReal toAdd) {
		if (denominator == null)
			denominator = new BigReal(0);
		
		denominator = denominator.add(toAdd);
	}
	
	public void addToNumerator(Object value, BigReal toAdd) {
		if (numerator == null)
			numerator = new HashMap<Object, BigReal>();
		
		BigReal real = numerator.get(value);
		if (real == null) 
			real = new BigReal(0);
		
		real = real.add(toAdd);
		
		numerator.put(value, real);
	}
	
	public BigReal getP(Object value) {
		BigReal br = numerator.get(value);
		if (br == null) {
			return BigReal.ZERO;
		}
		return br.divide(denominator);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object key : numerator.keySet()) {
			sb.append(key).append(" = ").append(numerator.get(key).divide(denominator).doubleValue()).append(" / ");
		}
		return sb.toString();
	}
	
}
