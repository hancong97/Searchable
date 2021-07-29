package edu.upenn.cis455.mapreduce.job;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import java.util.HashMap;
import edu.upenn.cis455.mapreduce.Context;



import edu.upenn.cis455.mapreduce.PRJob;
import edu.upenn.cis455.mapreduce.worker.WorkerAdmin;



public class PageRankJob implements PRJob {


	DecimalFormat df = new DecimalFormat("#.0000000000");
	long totalNodes = 4;
	long allTotalNodes = 4;
 
 
	double lambda = 0.80;
 
 
	String SINK = "SINK";
	static Map<String, Double> iter2SinkRank = new HashMap<>();
 
 
	public static String urlSpliter = ";;;";
 
 
	static String urlPrefix = "www";
	Double sinkRankShare = null;




	@Override
	public void map(String key, String normalizedRankAndOutUrlsOrSink, Context context){
		



		
		int underscoreIdx = key.indexOf("_");
   
   
		String url = null;
		Integer iter = 1;
		if (underscoreIdx != -1) {
   
   
			iter = Integer.parseInt(key.substring(0, underscoreIdx));
      
      
			url = key.substring(underscoreIdx+1);
		}
		System.out.println("[PageRankJob_" +iter+ "]: map(), read:  { " + key + " | " + normalizedRankAndOutUrlsOrSink + "}");
	
		Double normalizedRank = null;
		if (iter == 1) {
			normalizedRank = 1.0 / totalNodes;
      
      
		} 
		String outUrlsOrSink = null;
		String[] outUrlsArr = null;
   
   
		int hyphenIdx = normalizedRankAndOutUrlsOrSink.indexOf("-");
		if (hyphenIdx != -1 && normalizedRankAndOutUrlsOrSink.startsWith(".")) {
   
   
			normalizedRank = Double.parseDouble(normalizedRankAndOutUrlsOrSink.substring(0, hyphenIdx));



			outUrlsOrSink = normalizedRankAndOutUrlsOrSink.substring(hyphenIdx+1);
		} else {
			outUrlsOrSink = normalizedRankAndOutUrlsOrSink;
      
      
      
		}
		outUrlsArr = outUrlsOrSink.split(urlSpliter);
		
				
	
 
 
		if(!outUrlsOrSink.startsWith(SINK) && outUrlsArr != null && normalizedRank != null ) {



			try {
			
      
      
				addRank(normalizedRank,  iter.toString());
			} catch (IOException e) {



				e.printStackTrace();
			}
			



			
			Double avgRank = normalizedRank / outUrlsArr.length;
      
      
			String avgRankStr = df.format(avgRank);
      
      
			for(String outUrl: outUrlsArr) {
      
      
				context.write(iter+"_"+outUrl, avgRankStr);
			
      
      
			}
		} 
		
   
   
		context.write(iter+"_"+url, outUrlsOrSink);
   
   



		
		
	}
	
	



	@Override
	public void reduce( String url, Iterator<String> values, Context context) {
		
		System.out.println("[PageRankJob]: reduce(), read:  { " + url + " |  values }");
		boolean isLastIter = false;
		




		Double rank = new Double(0);
   
   
		String outUrlsOrSink = SINK;
				
		int uderscoreIdx = url.indexOf("_");
		Integer iter = Integer.parseInt(url.substring(0, uderscoreIdx));
		url = url.substring(uderscoreIdx + 1);
		
		while (values.hasNext()) {
   
   
			String value = values.next();
			
      
      
			if(value.startsWith(".")) {


		
				try {
					rank += Double.parseDouble(value);
				} catch(Exception e){
					
				}
        
        
        
			} else {
				if (isLastIter) {
        
        
					continue;
				}
		
   
				if (value.length() == 0) {
					outUrlsOrSink = SINK;
				} else {
					outUrlsOrSink = value;
				}



			}
		}
		



		double S = 0.0; 
   
   
		try {
			S = 1 - (double) getRank(iter);
      
      
		} catch (IOException e) {
			e.printStackTrace();
		}




		this.sinkRankShare =  (1.0 - lambda + lambda * S) / allTotalNodes;
		Double normalizedRank = lambda * rank + this.sinkRankShare;
   
		String normalizedRankStr = df.format(normalizedRank);
   



		
		String nextIter = String.valueOf(iter+1);




		context.write(nextIter + "_" + url, normalizedRankStr+"-"+outUrlsOrSink);
	}



	@Override 
	public synchronized Double getRank(Integer iter) throws IOException {
	
	        StringBuilder sb = new StringBuilder();
                 
                 
	        sb.append(WorkerAdmin.masterLocation + "/getrank?");
	        sb.append("iter=" + iter);
                 
                 
	        URL url = new URL(sb.toString());
	
	        HttpURLConnection conn;
	        conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("GET");
                 
                 
	        conn.connect();
	        
                 
	        InputStream in = conn.getInputStream();
	        String encoding = conn.getContentEncoding();
	        encoding = encoding == null ? "UTF-8" : encoding;
	        String rankSum = IOUtils.toString(in, encoding);
                 
                 
	        Double sum = 0.8;
	        try {
	        	sum = Double.parseDouble(rankSum);
	        	if (sum > 1 || sum == null) {
		        	sum = 0.8;
		        }
	        } catch (Exception e){
	        	sum = 0.8;
	        } 



	        return sum;
	}
	
	



	@Override 
	public synchronized void addRank(double rank, String iter) throws IOException {
			System.out.println("[PageRankJob]: addRank(), rank = " + rank);
      
      
	        StringBuilder sb = new StringBuilder();
	        sb.append(WorkerAdmin.masterLocation + "/addrank?");
	        sb.append("iter=" + iter);
                 
                 
	        sb.append("&rank=" + rank);
	        URL url = new URL(sb.toString());
                 
                 
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.connect();
	        conn.getResponseCode();
	        
	}


}
