package com.sewn.pagerank;

import java.util.List;


public class ResultPrinter {

    public static String PrintInlinks(List<Row> links, Statistics st)
    {
        return PrintLinks(links, st, true);
    }
    
    public static String PrintOutlinks(List<Row> links, Statistics st)
    {
        return PrintLinks(links, st, false);
    }
    
    public static String PrintPageRank(List<Result> pr, int it)
    {
        return PrintPR(pr, it);
    }

    private static String PrintPR(List<Result> pr, int it)
    {
        String text = "   #iterations:\t<"+it+">\n";
        
        for (Result child : pr) {
            text += "\t<" + child.getURL() + ">\t<"+child.getPageRank()+">";
            text += "\n";
        }
        return text;
    }
    
    
    private static String PrintLinks(List<Row> list, Statistics st, Boolean type)
    {
        String text = "";
        if (type ==true) {
        	text += "   #inlinks\t#pages\n";
        } else {
        	text += "   #outlinks\t#pages\n";
        }
        text += "---------------------------------------------------------------------------------------------\n";
        
        for (Row child : list) {
            text += "\t<" + child.getLinks() + ">\t"+child.getPage();
            text += "\n";
        }
        text += "\n";
        text += " #statistics\n";
        text += "------------------------------\n";
        text += " Mean:  "+st.getMean()+"\n";
        text += " Standard deviation:  "+st.getStdev()+"\n";
        text += " Variance:  "+st.getVariance()+"\n";
        return text;
    }

}
