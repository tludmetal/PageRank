package com.sewn.pagerank;

public class Statistics {
	
    private double mean;

    private double variance;

    private double stdev;
    
    public String ToString()
    {
      return "Mean: " + this.mean + "\nVariance: " + this.variance + "\nStandard deviation: " + this.stdev;
    }

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public double getStdev() {
		return stdev;
	}

	public void setStdev(double stdev) {
		this.stdev = stdev;
	}

}
