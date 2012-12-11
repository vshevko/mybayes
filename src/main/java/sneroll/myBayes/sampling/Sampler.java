package sneroll.myBayes.sampling;

import java.util.Random;
import java.util.concurrent.Callable;

public interface Sampler extends Callable<String> {

	public static final Random random = new Random(System.currentTimeMillis());

	public abstract Sampler setIterations(int iterations);

	public abstract Sampler setBurin(int burin);

	public abstract Sampler setSamplingFreq(int samplingFreq);

	//public abstract void sample();

}