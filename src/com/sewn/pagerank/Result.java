package com.sewn.pagerank;

import java.util.Comparator;

public class Result implements Comparator<Result> {
	
    public Result() {
		super();
		// TODO Auto-generated constructor stub
	}
	private double pg;
    private String URL;
    
    public Result(String URL, double pagerank)
    {
      this.URL = URL;
      this.pg = pagerank;
    }

    @Override
    public int compare(Result other, Result other2)
    {
      return -1 * ((this.pg==other.pg)? 1 : 0);
    }

    public double getPageRank()
    {
      return this.pg;
    }

    public int compareTo(Object obj)
    {
      return this.compareTo((Result) obj);
    }

    public String toString()
    {
      return this.URL + "\t" + this.pg;
    }

    public Boolean equals(Result other)
    {
      if (other == null)
        return false;
      if (this==other)//(this instanceof other)
        return true;
      else
        return this.URL.equals(other.URL);
    }
    
    public double getPg() {
		return pg;
	}
	public void setPg(double pg) {
		this.pg = pg;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}

}
