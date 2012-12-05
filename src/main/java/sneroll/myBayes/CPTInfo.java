package sneroll.myBayes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.util.BigReal;

public class CPTInfo {

	private Map<Object, BigReal> calculatedP = new HashMap<Object, BigReal>();
	private Map<Object, BigReal> numerator;
	private BigReal denominator = BigReal.ONE;
	
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
	
	private void addToTotalNumber(BigReal toAdd) {
		if (denominator == null)
			denominator = BigReal.ZERO;
		
		denominator = denominator.add(toAdd);
	}
	
	public void addToExpectedNumer(Object value, BigReal toAdd) {
		if (numerator == null)
			numerator = new HashMap<Object, BigReal>();
		
		BigReal real = numerator.get(value);
		if (real == null) 
			real = BigReal.ZERO;
		
		real = real.add(toAdd);
		
		numerator.put(value, real);
		
		addToTotalNumber(toAdd);
	}
	
	public BigReal getP(Object value) {
		BigReal p = calculatedP.get(value);
		if (p == null) {
			BigReal br = numerator.get(value);
			if (br == null) {
				return BigReal.ZERO;
			}
			p = br.divide(denominator);
			calculatedP.put(value, p);
		}
		return p;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object key : numerator.keySet()) {
			sb.append("\n").append(key).append(" = ").append(getP(key).doubleValue());
		}
		return sb.toString();
	}
	
}
