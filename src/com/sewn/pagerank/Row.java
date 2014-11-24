package com.sewn.pagerank;

public class Row {
	
	private int links;
    private String page;
    
    public Row(int ocurrences, String page)
    {
      this.links = ocurrences;
      this.page = page;
    }

    public String toString()
    {
      return this.links + "\t" + this.page;
    }
    
	public int getLinks() {
		return links;
	}
	public void setLinks(int links) {
		this.links = links;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
    
}
