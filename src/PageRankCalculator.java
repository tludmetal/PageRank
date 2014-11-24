

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sewn.pagerank.Graph;
import com.sewn.pagerank.Result;
import com.sewn.pagerank.ResultPrinter;
import com.sewn.pagerank.Row;
import com.sewn.pagerank.Statistics;

public class PageRankCalculator {

	private static double T = 0.15;
	private static Graph graph;
	private static Statistics outs;
	private static Statistics ins;
	private static int iterations;
	private static int maxiters;
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		if (args.length<2){
			System.out.println("You must introduce 2 parameters");
			System.out.println("Please introduce T and Iterations, e.g. make.bat 0.15 20");
			System.exit(-1);
		}
		System.out.println("You chose T="+args[0]);
		System.out.println("You chose "+args[1]+ " iterations");
		setT(Double.parseDouble(args[0]));
		setMaxIters(Integer.parseInt(args[1]));
		PageRankCalculator.CalculatePR("http://www.dcs.bbk.ac.uk/~martin/sewn/ls4/sewn-crawl-2014.txt");

		BufferedReader reader;
		String lineRead;
		File il = new File("..\\inlinks.txt");
		File ol = new File("..\\outlinks.txt");
		File pr = new File("..\\pagerank"+args[0].replaceAll("[.]","") +".txt");
		List<Row> inlinks=getInlinks();
		List<Row> outlinks=getOutlinks();
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("To print inlinks.txt press enter...");
		lineRead = reader.readLine();
		String inContent=ResultPrinter.PrintInlinks(inlinks, getStatsIn());
		System.out.println(inContent);
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("To print outlinks.txt press enter...");
		lineRead = reader.readLine();
		String outContent=ResultPrinter.PrintOutlinks(outlinks, getStatsOut());
		System.out.println(outContent);
		List<Result> res= getPageRank();
		reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("To print pagerank"+args[0].replaceAll("[.]","") +".txt press enter...");
		lineRead = reader.readLine();
		String prContent=ResultPrinter.PrintPageRank(res, PageRankCalculator.iterations);
		System.out.println(prContent);
	
		try (FileOutputStream fop = new FileOutputStream(il)) {
			 
			// if file doesn't exists, then create it
			if (!il.exists()) {
				il.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = inContent.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (FileOutputStream fop = new FileOutputStream(ol)) {
			 
			// if file doesn't exists, then create it
			if (!ol.exists()) {
				ol.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = outContent.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (FileOutputStream fop = new FileOutputStream(pr)) {
			 
			// if file doesn't exists, then create it
			if (!pr.exists()) {
				pr.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = prContent.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Three files were generated on the root of the project: inlinks.txt, outlinks.txt and pagerank"+args[0].replaceAll("[.]","") +".txt");
	}
	

	public static void CalculatePR(String graphURL) {
		PageRankCalculator.graph = new Graph(graphURL);
	}

	public static void setT(double newT) {
		PageRankCalculator.T = newT;
	}

	public static List<Row> getlinks(List<List<Integer>> linkcollection,
			Statistics statistics) {
		List<Row> list = new ArrayList<Row>();
		double num1 = 0.0;
		double num2 = 0.0;
		for (int index = 0; index < linkcollection.size(); index++) {
			int ocurrences = linkcollection.get(index) == null ? 0
					: linkcollection.get(index).size();
			num1 += (double) ocurrences;
			num2 += (double) (ocurrences * ocurrences);
			list.add(new Row(ocurrences,
					PageRankCalculator.graph.nodesURLs[index]));
		}
		double num3 = num1 / (double) PageRankCalculator.graph.outLinks.size();
		double d = num2 / (double) PageRankCalculator.graph.outLinks.size()
				- num3 * num3;
		double num4 = Math.sqrt(d);
		statistics.setMean(num3);
		statistics.setStdev(num4);
		statistics.setVariance(d);
		return list;
	}

	public static List<Row> getInlinks() {
		PageRankCalculator.ins = new Statistics();
		return PageRankCalculator.getlinks(PageRankCalculator.graph.inLinks,
				PageRankCalculator.ins);
	}

	public static List<Row> getOutlinks() {
		PageRankCalculator.outs = new Statistics();
		return PageRankCalculator.getlinks(PageRankCalculator.graph.outLinks,
				PageRankCalculator.outs);
	}

	public static Statistics getStatsOut() {
		return PageRankCalculator.outs;
	}

	public static Statistics getStatsIn() {
		return PageRankCalculator.ins;
	}

	public static List<Result> getPageRank() {
		List<Result> list = new ArrayList<Result>();
		double num1 = (double) PageRankCalculator.graph.inLinks.size();
		double[] pagerank = new double[PageRankCalculator.graph.inLinks.size()];
		//int num2 = 0;
		PageRankCalculator.iterations = 0;
		do {
			//double[] previous = (double[]) pagerank.clone();
			for (int index = 0; index < PageRankCalculator.graph.inLinks.size(); ++index) {
				pagerank[index] = PageRankCalculator.T / num1;
				if (PageRankCalculator.graph.inLinks.get(index) != null) {
					for (Integer num3 : PageRankCalculator.graph.inLinks.get(index))
						pagerank[index] += (1.0 - PageRankCalculator.T)
								* pagerank[(int) num3]
								/ (double) PageRankCalculator.graph.outLinks.get(num3).size();
				}
			}
			++PageRankCalculator.iterations;
//			if (PageRankCalculator.adaptative) {
//				if (!PageRankCalculator.orderchanged(pagerank, previous))
//					++num2;
//				else
//					num2 = 0;
//				if (num2 > PageRankCalculator.maxloops)
//					break;
//			}
		} while (PageRankCalculator.iterations < PageRankCalculator.maxiters);
		for (int index = 0; index < pagerank.length; ++index)
			list.add(new Result(PageRankCalculator.graph.nodesURLs[index],
					pagerank[index]));
		Collections.sort(list, new Result());
		return list;
	}

	public static int getIterations() {
		return PageRankCalculator.iterations;
	}

//	public static Boolean orderchanged(double[] pagerank, double[] previous)
//    {
//      List<Result> list1 = new ArrayList<Result>();
//      for (int index = 0; index < pagerank.length; ++index)
//    	  list1.add(new Result(PageRankCalculator.graph.nodesURLs[index], pagerank[index]));
//      Collections.sort(list1, new Result());
//      List<Result> list2 = new ArrayList<Result>();
//      for (int index = 0; index < pagerank.length; ++index)
//    	  list2.add(new Result(PageRankCalculator.graph.nodesURLs[index], previous[index]));
//      Collections.sort(list2, new Result());
//      return !equals(list1,list2);
//    }
//	
//	public static boolean equals(List<Result> array,List<Result> array1){
//		if(array.size()!=array1.size()) return false; // test for different length
//		for(int i = 0; i < array1.size(); i++){
//		    if (array1.get(i).compare(array.get(i),null)==-1){
//		        return false;
//		    }       
//		 }
//		 return true;
//		}


	public static void setMaxIters(int p) {
		PageRankCalculator.maxiters = p;
	}


}
