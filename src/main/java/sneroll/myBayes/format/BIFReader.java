package sneroll.myBayes.format;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.math3.util.BigReal;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import sneroll.myBayes.BayesUtils;
import sneroll.myBayes.BayesianNetwork;
import sneroll.myBayes.CPTInfo;
import sneroll.myBayes.CPTKey;
import sneroll.myBayes.ConditionalProbabilityTable;
import sneroll.myBayes.Node;

public class BIFReader implements BayesianNetworkReader {
	
	private File file;
	private BayesianNetwork bn;
	private Map<Node, ConditionalProbabilityTable> cpts;
	
	public BIFReader(File file, BayesianNetwork bn, Map<Node, ConditionalProbabilityTable> cpts) {
		this.file = file;
		this.bn = bn;
		this.cpts = cpts;
		
	}
	
	public BIFReader(File file, BayesianNetwork bn) {
		this(file, bn, null);
	}
	
	
	/* (non-Javadoc)
	 * @see sneroll.myBayes.format.BayesianNetworkReader#parseDocument()
	 */
	public void parseDocument() {
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			//parse the file and also register this class for call backs
			sp.parse(file, new BIFDefaultHandler());
			
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	private class BIFDefaultHandler extends DefaultHandler {
		
		private String tempVal;
		private Map<String, Node> nodes = new HashMap<String, Node>();;
		
		private Node currentNode;
		
		//Event Handlers
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			//reset
			tempVal = null;
	
			if(qName.equalsIgnoreCase("VARIABLE") || qName.equalsIgnoreCase("DEFINITION")) {
				currentNode = null;
			}
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			tempVal = new String(ch,start,length);
		}
		
		public void endElement(String uri, String localName, String qName) throws SAXException {
	
			if(qName.equalsIgnoreCase("NAME")) {
				currentNode = new Node(tempVal);
				nodes.put(tempVal, currentNode);
				
			}else if (qName.equalsIgnoreCase("OUTCOME")) {
				currentNode.addPosibleValue(tempVal);
				
			}else if (qName.equalsIgnoreCase("FOR")) {
				currentNode = nodes.get(tempVal);
				if (currentNode == null)
					throw new RuntimeException();
				
				cpts.put(currentNode, new ConditionalProbabilityTable(currentNode));
				
			}else if (qName.equalsIgnoreCase("GIVEN")) {
				Node parent = nodes.get(tempVal);
				if (parent == null)
						throw new RuntimeException();
				
				bn.addEdge(parent, currentNode);
				
			}else if (qName.equalsIgnoreCase("TABLE")) {
				
				if (cpts != null) {
					
					StringTokenizer st = new StringTokenizer(tempVal, " ");
					ConditionalProbabilityTable cpt = cpts.get(currentNode);
					
					// Requires this order of nested FORs
					for (CPTKey key : BayesUtils.getAllKeys(currentNode)) {
						CPTInfo info = cpt.getCPTInfo(key);
						for (Object pv : currentNode.getPosibleValues()) {
							String p = st.nextToken();
							info.addToExpectedNumer(pv, new BigReal(p));
						}
					}
				}
			}
		}
	}
	
}
