package SearchEngine;

import static spark.Spark.*;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;


import java.util.concurrent.ExecutorService;


import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.json.JSONObject;


import queryDataBase.RequestData;

import edu.upenn.cis455.indexer.storage.*;
import findCloseWord.CloseWordsDict;
import LRUCacheQueries.LRUCache;
import api.*;

public class searchEngine {


	static Logger log = Logger.getLogger(searchEngine.class);
	
	public static HashMap<String,Integer> frequencyMapping = new HashMap<>();
  //public static HashMap<String,String[]> urlinfo = new HashMap<String,String[]>();
	private static final String BLANK = " ";
	public static CloseWordsDict closewordsdict;
	public static removeStopWords removestopwords;
	public static LRUCache LRUCache;


	public static WeatherApi weatherapi;
 
 
	public static yelpApi yelpapi;
 
 
	public static DBIndexer DBINDEXER0_0;
	public static DBIndexer DBINDEXER0_1;
	public static DBIndexer DBINDEXER0_2;
	public static DBIndexer DBINDEXER0_3;
	public static DBIndexer DBINDEXER0_4;
	public static DBIndexer DBINDEXER0_5;
	
	public static DBIndexer DBINDEXER1_0;
	public static DBIndexer DBINDEXER1_1;
	public static DBIndexer DBINDEXER1_2;
	public static DBIndexer DBINDEXER1_3;
 
 
	public static DBIndexer DBINDEXER1_4;
	public static DBIndexer DBINDEXER1_5;
	
	public static DBIndexer DBINDEXER2_0;
	public static DBIndexer DBINDEXER2_1;
	public static DBIndexer DBINDEXER2_2;
	public static DBIndexer DBINDEXER2_3;
 
	public static DBIndexer DBINDEXER2_4;
	public static DBIndexer DBINDEXER2_5;
	
	public static DBIndexer DBINDEXER3_0;
	public static DBIndexer DBINDEXER3_1;
	public static DBIndexer DBINDEXER3_2;
	public static DBIndexer DBINDEXER3_3;
	public static DBIndexer DBINDEXER3_4;
	public static DBIndexer DBINDEXER3_5;

	public static HashSet<String> cities;
	public static HashSet<String> companies;
	public static String crawlerHost;
	public static String pagerankHost;

	public static ConcurrentLinkedQueue<ResultEntry> temp = new ConcurrentLinkedQueue<>();


	
	public searchEngine() {
		//api
		cities = new HashSet<>();
		companies = new HashSet<>();
		this.weatherapi = new WeatherApi();
		this.yelpapi = new yelpApi();
		//dict
		this.removestopwords = new removeStopWords();
		this.LRUCache = new LRUCache(1000);
		this.closewordsdict = new CloseWordsDict();
		
		this.DBINDEXER0_0 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_0_0/DBIndexer");
		this.DBINDEXER0_1 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_0_1/DBIndexer");
		this.DBINDEXER0_2 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_0_2/DBIndexer");
   
		this.DBINDEXER0_3 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_0_3/DBIndexer");
		this.DBINDEXER0_4 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_0_4/DBIndexer");
		this.DBINDEXER0_5 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_0_5/DBIndexer");
		
		this.DBINDEXER1_0 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_1_0/DBIndexer");
		this.DBINDEXER1_1 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_1_1/DBIndexer");
		this.DBINDEXER1_2 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_1_2/DBIndexer");
   
		this.DBINDEXER1_3 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_1_3/DBIndexer");
		this.DBINDEXER1_4 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_1_4/DBIndexer");
		this.DBINDEXER1_5 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_1_5/DBIndexer");
		
		this.DBINDEXER2_0 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_2_0/DBIndexer");
		this.DBINDEXER2_1 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_2_1/DBIndexer");
		this.DBINDEXER2_2 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_2_2/DBIndexer");
		this.DBINDEXER2_3 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_2_3/DBIndexer");
   
		this.DBINDEXER2_4 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_2_4/DBIndexer");
		this.DBINDEXER2_5 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker//worker_2_5/DBIndexer");
		
		this.DBINDEXER3_0 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_3_0/DBIndexer");
		this.DBINDEXER3_1 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_3_1/DBIndexer");
		this.DBINDEXER3_2 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_3_2/DBIndexer");
		this.DBINDEXER3_3 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_3_3/DBIndexer");
		this.DBINDEXER3_4 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_3_4/DBIndexer");
		this.DBINDEXER3_5 = new DBIndexer("/home/ubuntu/555projectdemo/SearchEngine/Cong_Worker/worker_3_5/DBIndexer");
		
		
	}
	
	public static void init() {
		searchEngine searchEngine = new searchEngine();
		staticFiles.location("/public");
	}
	
	
	public static void main(String[] args) {
		init(); 
		port(Integer.valueOf(args[0]));
		pagerankHost = args[1];
		crawlerHost = args[2];
		
		//routes
	    get("/",(request,response) -> {   
	    	response.type("text/html; charset=UTF-8");
	    	
	    	StringBuilder sb = new StringBuilder();
	    	sb.append("<!doctype html>\n" + 
	    			"<html lang=\"en\">\n" + 
                                     
                                     
	    			"  <head>\n" + 
	    			"    <!-- Required meta tags -->\n" + 
                               
                               
	    			"    <meta charset=\"utf-8\">\n" + 
	    			"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" + 
	    			"\n" + 
	    			"    <!-- Bootstrap CSS -->\n" + 
	    			"    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" + 
	    			"\n" + 
                       
                       
	    			"    <title>CIS 455/555 index page</title>\n" + 
	    			"\n" + 
	    			"     <!-- Bootstrap CSS style-->\n" + 
	    			"    <style>\n" + 
	    			"      body {\n" + 
	    			"        margin: 0;\n" + 
	    			"        padding: 0;\n" + 
	    			"        font-family: 'Roboto', sans-serif;\n" + 
	    			"	overflow: hidden;\n" + 
	    			"      }\n" + 
                          
                          
	    			"\n" + 
	    			"      header {\n" + 
	    			"        width: 100%;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      ul {\n" + 
	    			"        list-style: none;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      /* NAV BAR */\n" + 
                          
                          
	    			"      #nav_bar {\n" + 
	    			"        float: right;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      #nav_bar li {\n" + 
	    			"        display: inline-block;\n" + 
	    			"        padding: 8px;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      #nav_bar #sign_in {\n" + 
                                     
                                     
                                     
	    			"        background: #4887ef; \n" + 
	    			"        margin-right: 25px;\n" + 
	    			"        padding: 7px 15px;\n" + 
	    			"        border-radius: 3px; \n" + 
	    			"        font-weight: bold;\n" + 
	    			"      }\n" + 
                          
                          
	    			"\n" + 
	    			"      .nav-links {\n" + 
	    			"        color: #404040;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      a {\n" + 
	    			"        text-decoration: none;\n" + 
	    			"        color: inherit;\n" + 
	    			"      }\n" + 
	    			"\n" + 
                        
                        
	    			"      li.nav-links a:hover {\n" + 
	    			"        text-decoration: underline;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      #sign_in:hover { \n" + 
	    			"        box-shadow: 1px 1px 5px #999;\n" + 
	    			"      }\n" + 
                          
                          
	    			"\n" + 
	    			"      #sign_in {\n" + 
	    			"        color: #fff;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      /* GOOGLE AREA */\n" + 
	    			"      .google #google_logo {\n" + 
	    			"        text-align: center;\n" + 
	    			"        display: block;\n" + 
	    			"        margin: 0 auto;\n" + 
                               
                               
	    			"        clear: both;\n" + 
	    			"        padding-top: 112px;\n" + 
	    			"        padding-bottom: 20px;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      .form {\n" + 
	    			"        text-align: center;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      #form-search { \n" + 
                                  
                                  
	    			"        width: 450px;\n" + 
	    			"        line-height: 32px;\n" + 
	    			"        padding: 20px 10px;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      .form #form-search {\n" + 
	    			"        padding: 0 8px;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      /*#form-search:hover {\n" + 
                                             
                                             
	    			"        border-color: #e4e4e4;\n" + 
	    			"        padding-top: 0;\n" + 
	    			"      }*/\n" + 
	    			"\n" + 
	    			"      .buttons {\n" + 
	    			"        text-align: center;\n" + 
	    			"        padding-top: 30px;\n" + 
                                           
                                           
	    			"        margin-bottom: 300px;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      /* FOOTER */\n" + 
	    			"      footer  {\n" + 
	    			"        background: #f2f2f2;\n" + 
	    			"        border-top: solid 2px #e4e4e4;\n" + 
	    			"      /*   position: fixed; */\n" + 
	    			"        bottom: 0;\n" + 
	    			"        padding-bottom: 0;\n" + 
                                           
                                           
	    			"        width: 100%;\n" + 
	    			"        \n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      footer ul li {\n" + 
	    			"        display: inline;\n" + 
	    			"        color: #666666;\n" + 
                                        
                                        
	    			"        font-size: 14px;\n" + 
	    			"        padding: 13px;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      footer ul {\n" + 
	    			"        float: left;\n" + 
	    			"        padding: 1px;\n" + 
                                      
                                      
	    			"      }\n" + 
	    			"\n" + 
	    			"      footer ul a:hover {\n" + 
	    			"        text-decoration: underline;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      .footer-right {\n" + 
	    			"        float: right;\n" + 
	    			"      }\n" + 
	    			"\n" + 
                        
                        
	    			"      /* MEDIA QUERIES */\n" + 
	    			"\n" + 
	    			"      @media screen and (max-width: 400px) {\n" + 
	    			"      \n" + 
	    			"      li.nav-links a {\n" + 
	    			"          display: none;\n" + 
	    			"        }\n" + 
	    			"        \n" + 
	    			"      #google_logo {\n" + 
	    			"        padding: 0;\n" + 
                                    
                                    
	    			"      }\n" + 
	    			"        \n" + 
	    			"      .buttons {\n" + 
	    			"        display: none;\n" + 
	    			"      }\n" + 
	    			"        \n" + 
	    			"      #form-search {\n" + 
                                     
                                     
	    			"        width: 80%;\n" + 
	    			"\n" + 
	    			"      }\n" + 
	    			"        \n" + 
	    			"      footer {\n" + 
	    			"        bottom: 0;\n" + 
	    			"      }\n" + 
	    			"        \n" + 
	    			"      footer ul {\n" + 
	    			"        float: none;\n" + 
                                     
                                     
	    			"        padding-bottom: 2px;\n" + 
	    			"          \n" + 
	    			"      }\n" + 
	    			"        \n" + 
	    			"      .footer-left {\n" + 
	    			"        text-align: center;\n" + 
	    			"        margin: auto; \n" + 
                                   
                                   
	    			"        padding-top: 10px;\n" + 
	    			"          \n" + 
	    			"      }\n" + 
	    			"        \n" + 
	    			"      .footer-right {\n" + 
	    			"        float: none;\n" + 
	    			"        text-align: center;\n" + 
                                            
                                            
	    			"        \n" + 
	    			"      }\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      @media screen and (max-width: 565px) {\n" + 
	    			"      \n" + 
	    			"        li.nav-links a {\n" + 
                                    
	    			"          display: none;\n" + 
	    			"        }\n" + 
	    			"        \n" + 
	    			"        \n" + 
	    			"      #google_logo {\n" + 
	    			"        padding: 0;\n" + 
	    			"      }\n" + 
	    			"        \n" + 
	    			"      .buttons {\n" + 
	    			"        display: none;\n" + 
	    			"      }\n" + 
                          
                          
	    			"        \n" + 
	    			"      #form-search {\n" + 
	    			"        width: 80%;\n" + 
	    			"\n" + 
	    			"      }\n" + 
	    			"       \n" + 
	    			"  }\n" + 
	    			"    </style>\n" + 
	    			"  </head>\n" + 
                            
	    			"\n" + 
	    			"  <body>\n" + 

	    			"    \n" + 
	    			"    <!-- GOOGLE IMG -->  \n" + 
	    			"    <div class=\"google\">\n" + 
                                           
                                           
	    			"     <a  id=\"google_logo\" ><img src=\"logo.jpeg\" alt=\"logo\" border=\"0\"></a>\n" + 
	    			"    </div>\n" + 
	    			"    \n" + 
	    			"    <!-- FORM SEARCH -->  \n" + 
	    			"    <div class=\"form\">  \n" + 
	    			"      <form  method=\"GET\"  action=\"search\">\n" + 
	    			"        <label for=\"form-search\"></label>\n" + 
                                                            
                                                            
	    			"        <input type=\"text\" id=\"form-search\" name=\"query\" placeholder=\"Search now or type URL\">\n" + 
	    			"        <div class= \"buttons\">  \n" + 
	    			"          <input type=\"submit\" value=\"Go Search\" id=\"penn_search\">\n" + 
	    			"          <a href=\"https://www.cis.upenn.edu/~cis455/\"><input type=\"button\"value=\"CIS555 Go!\" id=\"55555\"></a>\n" + 
                                                                             
                                                                             
	    			"        </div>\n" + 
	    			"      </form>\n" + 
	    			"    </div>  \n" + 
	    			"    \n" + 
	    			" \n" + 
	    			"      \n" + 
	    			"\n" + 
	    			"\n" + 
	    			"    <!-- Optional JavaScript -->\n" + 
	    			"    <!-- jQuery first, then Popper.js, then Bootstrap JS -->\n" + 
                             
                             
	    			"    <script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" + 
	    			"    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\n" + 
	    			"    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>\n" + 
	    			"  </body>\n" + 
	    			"</html>");
	    	return sb.toString();
               
               
	    });



	    get("/search",(request,response) -> {   
         long startTime = System.nanoTime();
	    	RequestData rd = new RequestData();
                    
                    
	    	response.type("text/html; charset=UTF-8");
	    	String query = "";
	    	boolean showSuggestWord = false;
	    	String suggestWords = null;
	    	StringBuilder suggestionSB = new StringBuilder();
	    	String newquery = "";
	    	HashMap<String, Integer> keysFrequency = new HashMap<>();
	    	if(!request.queryParams().isEmpty()) {
	    		query = request.queryParams("query");
	    		log.info("user input query : " + query + "  query's length: " + query.length()) ;
	    		if(query.trim().length() != 0)

	    		newquery = removestopwords.removeStopWords(query).trim();
	    		

	    		String[] words = splitwords(newquery);
	    		keysFrequency = decompose(words);

	    		for(int i = 0; i < words.length;i++) {
	    			log.debug(words[i] + " is found in the dict ? return: { " + closewordsdict.getTrie().search(words[i]) + "}");



	    			if(closewordsdict.getTrie().search(words[i])) {
                                                                  
                                                                  
	    				suggestionSB.append(words[i]);
	    			} else {
	    				String temp = searchCloseWords(words[i]);
	    				if(temp == null || temp.trim().length() == 0) {
	    					suggestWords = correct(words[i]);
                                                 
                                                 
	    				}else {
	    					suggestWords = correct(temp);
                                             
                                             
	    				}
	    				
	    				if(suggestWords == null || suggestWords.contains("null")) {
                                      
                                      
	    					suggestionSB.append(words[i]);
	    				}else {
	    					showSuggestWord = true;
	    					String[] suggestWordsList = suggestWords.split("\\s+");
                                            
                                            
	    					suggestionSB.append(suggestWordsList[0]);
	    				}
	    			}
	    			suggestionSB.append(BLANK);
	    		}
	    		
	    		log.info("[new query generated] : " + suggestionSB.toString());
                               
                               
	    		if(suggestionSB.toString().length() == 1 && suggestionSB.toString().equals(" ")) showSuggestWord = false;
                                                 
                                                 
	    		String[] newquerywords = suggestionSB.toString().split("\\s+");
	    		for(String word : newquerywords) put(word.trim());
	    	}
	    	

	    	log.info("start searching");



	    	for(String word : keysFrequency.keySet()) {
	    		int indexerDBnumber = getWordWorker(word);
	    		IndexEntry wordEntry = null;
	    		switch (indexerDBnumber) {
				  case 0:
                 
					wordEntry =  new IndexEntry(word);
					wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER0_0);
					wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER0_1);
					wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER0_2);
					wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER0_3);
					wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER0_4);
					wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER0_5);
				    break;
				  case 1:
					  wordEntry =  new IndexEntry(word);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER1_0);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER1_1);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER1_2);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER1_3);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER1_4);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER1_5);
				    break;
				  case 2:
					  wordEntry =  new IndexEntry(word);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER2_0);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER2_1);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER2_2);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER2_3);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER2_4);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER2_5);
				    break;
				  case 3:
					  wordEntry =  new IndexEntry(word);
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER3_0); 
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER3_1); 
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER3_2); 
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER3_3); 
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER3_4); 
					  wordEntry = parseBothINDEXER(word, wordEntry,DBINDEXER3_5); 
				    break;
				}
	    	
	    		
	    	}
	    	log.debug("now validating");


	    	StringBuilder sb = new StringBuilder();
	    	// 
	    	sb.append("<!doctype html>\n" + 
	    			"<html lang=\"en\">\n" + 
	    			"  <head>\n" + 
                           
                           
	    			"    <!-- Required meta tags -->\n" + 
	    			"    <meta charset=\"utf-8\">\n" + 
	    			"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" + 
	    			"\n" + 
	    			"    <!-- Bootstrap CSS -->\n" + 
	    			"    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" + 
                                          
                                          
	    			"    <title>CIS 455/555 search page</title>");
	    	
	    	
	    	sb.append("<style>\n" + 
	    			"      body {\n" + 
	    			"        margin: 0;\n" + 
	    			"        padding: 0;\n" + 
	    			"        font-family: 'Roboto', sans-serif;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      header {\n" + 
	    			"        height: 45px;\n" + 
	    			"        width: 100%;\n" + 
	    			"      }\n" + 
                          
	    			"\n" + 
	    			"      ul {\n" + 
	    			"        list-style: none;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      /* NAV BAR */\n" + 
	    			"      #nav_bar li {\n" + 
	    			"        display: inline-block;\n" + 
	    			"        padding: 8px;\n" + 
	    			"      }\n" + 
	    			"\n" + 
                        
                        
	    			"      #nav_bar input {\n" + 
	    			"        display: inline-block;\n" + 
	    			"        padding: 8px;\n" + 
	    			"        width: 50rem;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"\n" + 
                        
                        
	    			"      .nav-links {\n" + 
	    			"        color: #404040;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      a {\n" + 
	    			"        text-decoration: none;\n" + 
	    			"        color: inherit;\n" + 
                                        
                                        
	    			"      }\n" + 
	    			"\n" + 
	    			"      .entry{\n" + 
	    			"        float: left; \n" + 
	    			"        width: 100%;\n" + 
	    			"        padding: 5px;\n" + 
	    			"      }\n" + 
                          
                          
	    			"\n" + 
	    			"      .result-stats{\n" + 
	    			"          color: darkgray;\n" + 
	    			"      }\n" + 
	    			"\n" + 
	    			"      .results{\n" + 
                                  
                                  
	    			"        display: none;\n" + 
	    			"      }\n" + 
	    			"     \n" + 
	    			"    </style>");
	    	
	    	sb.append(" <body>\n" + 
	    			"\n" + 
	    			"        <div class=\"container-fluid\">\n" + 
                                                        
                                                        
	    			"            <div class=\"header\">\n" + 
	    			"                <nav>\n" + 
                                        
                                        
	    			"                    <ul id=\"nav_bar\" >\n" + 
	    			"                      <li><img  src=\"logo.jpeg\" alt=\"logo\" border=\"0\" style = \"height:50px;\"></li>\n" + 
	    			"                      <li>\n" + 
	    			"                          <form  method=\"GET\"  action=\"search\">\n" + 
	    			"                              <label for=\"form-search\"></label>\n" + 
	    			"                              <input type=\"text\" id=\"form-search\" name=\"query\" placeholder=\"");
	    	if(newquery.length() == 0) {
                             
                             
	    		sb.append("Search on search or type URL\"");
	    	} else {
	    		sb.append(query);
	    	}
	    			
	    			
	    	sb.append("\"/>\n" );
	    	sb.append("</form>\n" + 
	    			"                      </li>\n" + 
	    			"                    </ul>  \n" + 
                                            
                                            
	    			"                  </nav> \n" + 
	    			"            </div>");
	    	
	    	
	    	if(showSuggestWord) {
	    		sb.append("<div>\n" + 
	    				"              <form  method=\"GET\"  action=\"search\">\n" + 
                                                    
                                                    
	    				"                Search instead for \n" + 
	    				"                <input type=\"submit\" name=\"query\" value = \""
	    				+ suggestionSB.toString()
	    				+ "\"/>    \n" + 
                             
                             
	    				"              </form>\n" + 
	    				"            </div>");
	    		
              
	    	}
	    		    

			//List<String> urllist = rd.getUrlList();
      //HashMap<String, String[]> urlinfo = rd.getTitlesAndContents();

      
      //urlinfo = rd.getTitlesAndContents();
	    	long endTime = System.nanoTime();
	    	double elapseTime = (endTime - startTime) /1_000_000_000 ;

	    	
	    	sb.append("<div class=\"result-stats\">\n" + 
	    			"                <p>Totally: "
	    			+  "20 "
	    			+ "results; Time : "
	    			+ elapseTime
	    			+ "s</p>\n" + 
	    			"            </div>");
	    	
	    	//data entry
	    	sb.append("<div class=\"row\">\n" + 
	    			"                <!-- all result entry -->\n" + 
	    			"                <div class=\"col-md-8 \" id=\"results\">\n" + 
	    			"                    <!-- list 20 result per page  -->");
	    	//20 -> dataResults.size()
             List<String> urllist = rd.getUrlList();
      HashMap<String, String[]> urlinfo = rd.getTitlesAndContents();
	    	int i=0;
			while(i<20) {
	    			String url = urllist.get(i);
            String[] result = urlinfo.get(url);
           // String title = urlinfo[i][0];
            //System.out.println("title: " + result[0]);
	    			sb.append("<div class = \"entry\">\n" + 
		    				"                        <div class = \"url\"><a href=\""
		    				+ url
		    				+"\">" + url +" >"
		    				+ "</a>"
		    				+ "</div>\n" + 
		    				"                        <div class = \"title\" style = \"color: blue;\"><h4><a href=\""
		    				+ url
		    				+ "\">"
		    				+ result[0]
		    				+ "</a> </h4></div>\n" + 
		    				"                        <div class = \"shortphoto\">"
		    				+ result[1]
		    				+ " </div>\n" + 
		    				"                   </div>");
	    			
	    		i++;
	    	}

	    	sb.append("</div>");
	    	
	    	//API 
	    	sb.append("<!-- api location -->\n" + 
	    			"                <div class=\"col-6 col-md-4\">");
	    	
	    	//weather api
	    	if(suggestionSB.toString().contains("weather")) {
	    		String cityOrState = null;
	    		for(String key : keysFrequency.keySet()) {
                         
                         
	    			if(cities.contains(key)) {
	    				cityOrState = key;
                                
                                
	    				break;
	    			}
	    		}
	    		if(cityOrState == null) cityOrState = "philadelphia";
	    		log.info(cityOrState);
                                  
                                  
 	    		weatherinfo info = null;
                           
                           
	    		if(cityOrState != null) {
	    			info = weatherapi.searchCurrent(cityOrState);
	    		} 
	    		
	    		sb.append("<div class=\"card\" style=\"width: 24rem;\">\n" + 
                                         
                                         
		    			"                      <div class=\"card-body\">\n" + 
		    			"                        <h5 class=\"card-title\" style=\"color:blue;\">Weather Api | "+
		    			info.state +
		    									"</h5>\n" + 
		    			"                        <h6 class=\"card-subtitle mb-2 text-muted\">" +
		    			"<img  src=\""
		    			+ info.icon
		    			+ "\" alt=\"logo\" border=\"0\" style = \"height:50px;\">"  + 
		    			info.weather + 
		    			 "</h6>\n" + 
                              
		    			"                        <div class=\"card-text\">\n" + 
		    			"<div>\n" + 
		    			"temperature now : " + info.value + "&#8451;"
		    			+"("
		    			+ info.min +"&#8451;"+ "-" + info.max + "&#8451;"
		    			+ ")"
		    			+ "\n" + 
		    			"                          </div>\n" + 
		    			"                          <div>\n" + 
		    			"                            \n" + 
		    			"                          </div>"+
		    			"                        <a href=\""
		    			+ "https://openweathermap.org/find?q="+ cityOrState
		    			+ "\" class=\"card-link\">weather link</a>\n" + 
		    			"                      </div>\n" + 
		    			"                    </div></div>");
	    		
	    		sb.append("<br>");
	    		
	    	}

	    	//yelp api
	    	if(suggestionSB.toString().contains("yelp")) {
	    		yelpInfo info = null;
	    		String company = "Starbucks";
                         
                         
	    		String location = "philadelphia";
	    		for(String key : keysFrequency.keySet()) {
                         
                         
	    			if(companies.contains(key)) {
	    				company = key;
	    				break;
	    			}
	    		}
	    		log.info("city size " + cities.size());
	    		for(String k : keysFrequency.keySet()) {
	    			log.info(k);
                                                 
                                                 
	    			if(cities.contains(k)) {
	    				location = k;
	    				break;
	    			}
	    		}
	    		log.info("select city-----" + location);
	    		JSONObject obj = yelpapi.connectToApi(company,location);
	    		
	    		if(obj != null) {
	    			info =   yelpapi.searchCurrent(obj);
	    			if(info!= null) {
	    				sb.append("<div class=\"card\" style=\"width: 24rem;\">\n" + 
				    			"                      <div class=\"card-body\">\n" + 
				    			"                        <h5 class=\"card-title\" style=\"color:blue;\">Yelp Api | "
                                                                    
                                                                    
				    			+  location
				    			+ "</h5>\n" +
				    			"<img  src=\""
				    			+ info.img
				    			+ "\" alt=\"logo\" border=\"0\" style = \"height:50px;\">"  +
				    			 "  "  + company + 
				    			"                        <h6 class=\"card-subtitle mb-2 text-muted\"> </h6>\n" + 
				    			"                        <p class=\"card-text\">" +
				    			"                          <div>\n" + 
                                                        
                                                        
				    			"rating: "+ info.rating             + 
				    			"                          </div>"+
				    			"                          <div>\n" + 
				    			 "phone number: " + info.phone             + 
				    			"                          </div>"+
                    "<div>\n" + 
				    			 "location: 1801 Market St" +
				    			"                          </div>"+
                     
				    			"</p>\n" + 
//				    			"                        <a href=\"\" class=\"card-link\">Card link</a>\n" + 
				    			"                      </div>\n" + 
				    			"                    </div> ");
	    				
	    			}
		    		
	    			
	    		}
	    		
	    	}
	    	
	    	
	    	
	    	// bottom
	    	sb.append("</div>\n" + 
	    			"              </div>\n" + 
                                       
                                       
	    			"        </div>");
	    	sb.append("<script src=\"https://code.jquery.com/jquery-3.4.1.slim.min.js\" integrity=\"sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n\" crossorigin=\"anonymous\"></script>\n" + 
	    			"<script src=\"https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js\" integrity=\"sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo\" crossorigin=\"anonymous\"></script>\n" + 
	    			"<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js\" integrity=\"sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6\" crossorigin=\"anonymous\"></script>\n" + 
	    			
	    			"    </body>\n" + 
	    			"</html>");
	    	return sb.toString();
	    });
	    
	}
	
	 private static IndexEntry parseBothINDEXER(String word, IndexEntry wordEntry, DBIndexer dBINDEXER02) {
		
		IndexEntry entry = dBINDEXER02.getIndexEntry(word);
   
		Map<String, ArrayList<String>> meta = new HashMap<>();
   
		if(entry != null) {
			meta = entry.getMeta();
      
			wordEntry.updateMeta(meta);
		}
		return entry;
	}

	public static int getWordWorker(String word) {
		  int ret = getWordWorker(word, 4, 100);

		  Map<Integer, Integer> route = new HashMap<>();
		  route.put(3, 1);
		  route.put(1, 0);
        
		  route.put(2, 2);
		  route.put(0, 3);
		  
		  return route.get(ret);
		  
	 }
	 public static int getWordWorker(String word, int workerNum, int executorNum) {
		  int ret = 0;

		  ret ^= word.hashCode();
        
		  int size = workerNum * executorNum;
		  ret = ret % size;
        
		  if (ret < 0)
		   ret = ret + size;
          

		  ret = ret / executorNum;
		  System.out.println(ret);
		  return ret;
	}
	 


	private static double scoreFormula(ResultEntry entry) {
		Double querytf = (double) ((double)entry.numWordsMatched/(double)entry.numWordsTotal);


		entry.score = (5 * entry.titleTF + entry.bodyTF) * querytf * 1000000;



		return entry.score;
	}

	private static void GETPageRankScore(ResultEntry newentry, ArrayList<ResultEntry> results) {
		results.add(newentry);
	}



	private static String searchCloseWords(String word) {
		word = word.trim().toLowerCase();
		ArrayList<String> neighbourWords = CloseWordsDict.getTrie().getWordsStartsWith(word, 100);
		if (neighbourWords.size() < 20) {
			int i = word.lastIndexOf(" ");
      
			log.info(i);
            if (i != -1) {
            
                String lastWord = word.substring(i + 1);
                ArrayList<String> advPred = CloseWordsDict.getTrie().getWordsStartsWith(lastWord, 1000);
                for (String s : advPred) {
                    String completed = word.substring(0, i + 1) + s;
                    if (!neighbourWords.contains(completed)) {
                    	neighbourWords.add(completed);
                    }
                }
            }
		}
		



		Collections.sort(neighbourWords, (o1, o2) -> {
            int freq1 = 0;
            if (frequencyMapping.containsKey(o1)) {
            
            
                freq1 = frequencyMapping.get(o1);
            }
            int freq2 = 0;
            if (frequencyMapping.containsKey(o2)) {
                freq2 = frequencyMapping.get(o2);
            }
            return freq2 - freq1;
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20 && i < neighbourWords.size(); i++) {
            sb.append(neighbourWords.get(i)).append(BLANK);
        }
        log.debug("close words : " + sb.toString());
        return sb.toString();
	}



    public static String correct(String query) {
        if (query == null) return null;
        String[] words = query.split("\\s+");
        if (words.length == 0) return null;
        String[] correction = new String[words.length];
        
        
        boolean corrected = false;
        for (int i = 0; i < words.length; i++) {
        
        
            if (frequencyMapping.containsKey(words[i]) && frequencyMapping.get(words[i]) > 1) {
            
            
                log.warn("No correction");
                correction[i] = words[i];
                
                
                continue;
            }
            int distance = words[i].length();
            String corrWord = words[i];
            for (String word : frequencyMapping.keySet()) {
                int dist = editDistance(word, words[i]);
                
                
                if (dist < distance && dist <= 3 && frequencyMapping.get(word) >= 1) {
                    distance = dist;
                    corrWord = word;
                }
            }
            if (distance >= 0) {
                correction[i] = corrWord;
                
                
                corrected = true;
            }
        }
        if (corrected) {
            StringBuilder sb = new StringBuilder(correction[0]);
            for (int i = 1; i < correction.length; i++) {
            
            
                sb.append(" ").append(correction[i]);
            }
            System.out.println(sb.toString());
            return sb.toString();
        } else {
            return null;
        }
    }
    




    private static int editDistance(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        for (int i = 0; i <= word1.length(); i++) {
        
        
            dp[i][0] = i;
        }
        for (int i = 0; i <= word2.length(); i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i <= word1.length(); i++) {
            for (int j = 1; j <= word2.length(); j++) {
            
            
                int k = 1;
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                
                
                    k = 0;
                }
                dp[i][j] = Math.min(dp[i - 1][j - 1] + k, Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
            }
        }
        return dp[word1.length()][word2.length()];
    }



    public static void put(String word) {
    	CloseWordsDict.getTrie().insert(word);
     
        if (!frequencyMapping.containsKey(word)) {
        	frequencyMapping.put(word, 1);
         
        } else {
        	frequencyMapping.put(word, frequencyMapping.get(word) + 1);
        }
    }




	private static HashMap<String, Integer> decompose(String[] words) {
		HashMap<String, Integer> res = new HashMap<>();
   
   
		for(String word : words) {
   
   
			word = word.toLowerCase();
			if(!res.containsKey(word)) {
      
      
				res.put(word,1);
			} else {
				res.put(word,res.get(word)+1);
			}
		}
		return res;
	}



	private static String[] splitwords(String newquery) {
 
		String[] words = newquery.split("\\s+");
		return words;
	}
}