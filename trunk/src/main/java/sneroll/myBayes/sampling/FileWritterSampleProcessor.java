package sneroll.myBayes.sampling;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class FileWritterSampleProcessor extends SampleProcessorAdapter {

	/**
	 * Output file where samples are going to be stored
	 */
	private File outputFile;
	/**
	 * Fields present in the data file that want to be saved in the output file
	 */
	private List<String> fields;
	
	private LinkedList<String> cacheLines = new LinkedList<String>();
	private static final int cacheLinesSize = 500;
	
	public FileWritterSampleProcessor(File outputFile, List<String> fields) {
		this.outputFile = outputFile;
		this.fields = fields;
		
		if (fields == null || fields.isEmpty()) {
			this.fields = null;
		}
	}

	@Override
	public void processSample(Map<String, Object> data) {
		if (fields == null) {
			this.fields = new ArrayList<String>(data.keySet());
		}
			
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String field : fields) {
			if (!first)
				sb.append("\t");
			sb.append(data.get(field));
			first = false;
		}
		cacheLines.add(sb.toString());
		
		if (cacheLines.size() > cacheLinesSize) {
			printToOutputFile();
		}
	}
	
	private void printToOutputFile() {
		try {
			FileUtils.writeLines(outputFile, cacheLines);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void end() {
		printToOutputFile();
	}
	
}
