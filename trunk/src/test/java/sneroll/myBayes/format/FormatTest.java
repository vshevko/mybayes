package sneroll.myBayes.format;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import sneroll.myBayes.BayesUtils;
import sneroll.myBayes.BayesianNetwork;
import sneroll.myBayes.ConditionalProbabilityTable;
import sneroll.myBayes.Node;

public class FormatTest {

	@Test
	public void test_BIFFormat_dogProblem() {
		
		BayesianNetwork bn = new BayesianNetwork();
		Map<Node, ConditionalProbabilityTable> cpts = new HashMap<Node, ConditionalProbabilityTable>();
		
		BayesianNetworkReader bifReader = new BIFReader(new File("src/test/resources/format/dog-problem.bif.xml"), bn, cpts);
		bifReader.parseDocument();
		
		System.out.println(BayesUtils.conditionalProbabilityTableToString(cpts));
		
	}
	
}
