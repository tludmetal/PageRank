package com.sewn.pagerank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Graph {

	private static final String _VISITED = "Visited: ";
	private static final String _LINK = "    Link: ";
	public List<List<Integer>> outLinks;
	public List<List<Integer>> inLinks;
	public String[] nodesURLs;
	private Integer current;

	public Graph(String url) {
		this.readGraph(url);
	}

	private void readGraph(String link) {
		URL url;
		HttpURLConnection webClient;
		InputStreamReader read = null;
		HashMap<String, Integer> dictionary = null;
		HashSet<Integer> hashSet = null;
		try {
			url = new URL(link);
			webClient = (HttpURLConnection) url.openConnection();
			InputStream data = webClient.getInputStream();
			read = new InputStreamReader(data);
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(read);
			String rd = br.readLine();
			while (rd != null) {
				sb.append(rd + "\n");
				rd = br.readLine();
			}

			String content = sb.toString();
			Pattern pattern = Pattern.compile(_VISITED+"(?<url>.*)");
			Matcher matchCollection = pattern.matcher(content);
			dictionary = new HashMap<String, Integer>();
			int index=0;
			//Creamos un diccionario con todos los link marcados como visitados, key url, value index
			while (matchCollection.find()) {
				if (!dictionary.containsKey(matchCollection.group().substring("Visited: ".length())))
					dictionary.put(matchCollection.group().substring("Visited: ".length()), index);
			    index++;
			}

			this.nodesURLs = new String[dictionary.size()];
			this.outLinks = new ArrayList<List<Integer>>();
			this.inLinks = new ArrayList<List<Integer>>();
			for (int i=0;i<dictionary.size();i++){
				this.outLinks.add(null);
				this.inLinks.add(null);
			}

			hashSet = new HashSet<Integer>();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			url = new URL(link);
			webClient = (HttpURLConnection) url.openConnection();
			InputStream data = webClient.getInputStream();
			read = new InputStreamReader(data);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (BufferedReader stringReader = new BufferedReader(read)) {
			String str;
			while ((str = stringReader.readLine()) != null) {
				if (str.startsWith("Visited: ")) {
					String index = str.substring("Visited: ".length());
					this.current = dictionary.get(index);
					this.nodesURLs[(int) this.current] = index;
					hashSet.clear();
					hashSet.add(this.current);
				} else if (str.startsWith("    Link: ")) {
					String key = this.validateURL(str.substring(_LINK.length()));
					if (dictionary.containsKey(key)
							&& !hashSet.contains(dictionary.get(key))) {
						Integer num = dictionary.get(key);
						hashSet.add(num);
						this.addOutlink(this.current, num);
						this.addInlink(num, this.current);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addInlink(Integer node, Integer inlink) {
		List<Integer> aux;
		if (this.inLinks.get(node)!=null)
			aux=this.inLinks.get(node);
		else
			aux=new ArrayList<Integer>();
		aux.add(inlink);
		this.inLinks.remove((int)node);
		this.inLinks.add(node,aux);
	}

	private void addOutlink(Integer node, Integer outlink) {
		List<Integer> aux;
		if (this.outLinks.get(node)!=null)
			aux=this.outLinks.get(node);
		else
			aux=new ArrayList<Integer>();
		aux.add(outlink);
		this.outLinks.remove((int)node);
		this.outLinks.add(node,aux);
	}

	private String validateURL(String link) {
		if (link.startsWith("http"))
			return link;
		else
			try {
				return getAbsoluteLink(new URL(
						this.nodesURLs[(int) this.current]), link);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "";
	}

	public String getAbsoluteLink(URL url, String link) {
		URL baseUrl;
		try {
			baseUrl = new URL(url.toString());
			URL auxurl = new URL(baseUrl, link);
			return auxurl.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}
