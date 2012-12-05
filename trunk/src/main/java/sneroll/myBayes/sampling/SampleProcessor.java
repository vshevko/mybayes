package sneroll.myBayes.sampling;

import java.util.Map;

public interface SampleProcessor {

	/**
	 * called when the sampler start
	 */
	void init();
	
	/**
	 * A new sample after burnin and sampling frequency have been produced. 
	 * (same sample is called in both processTempSample and here)
	 * @param data
	 */
	void processSample(Map<String, Object> data);
	
	/**
	 * A new sample have been processed. Burnin and sampling freq not taken into account.
	 * @param data
	 */
	void processTempSample(Map<String, Object> data);

	/**
	 * sampling have ended;
	 */
	void end();
	
}
