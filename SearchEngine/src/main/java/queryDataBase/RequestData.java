package queryDataBase;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
public class RequestData {
	
	public Random rand = new Random();
	public int[] temp = new int[20];
	//id - url
	public HashMap<Integer,String> urlid = new HashMap<Integer,String>();
	//url - <title, content>
    public HashMap<String,String[]> urlinfo = new HashMap<String,String[]>();

    
	public RequestData() {
     
	    int count = 0;  
	    while(count < 20) {  
	        int num = (int) (Math.random() * 100);  
	        boolean flag = true;  
	        for (int j = 0; j < 20; j++) {  
	            if(num == temp[j]){  
	                flag = false;  
	                break;  
	            }  
	        }  
	        if(flag){  
	            temp[count] = num;  
	            count++;  
	        }  
	    } 
         System.out.println("random okkkkkkkkkk");
		/*
        urlid.put(1, "https://www.cdc.gov/coronavirus/2019-ncov/index.html");
        urlid.put(2,"https://www.cdc.gov/coronavirus/2019-ncov/symptoms-testing/symptoms.html");
        urlid.put(3,"https://www.nytimes.com/2021/05/03/health/covid-herd-immunity-vaccine.html");
        urlid.put(4, "https://www.nhs.uk/conditions/coronavirus-covid-19/");
        urlid.put(5, "https://www.nytimes.com/interactive/2021/us/covid-cases.html");
        urlid.put(6, "https://www.health.pa.gov/topics/disease/coronavirus/Pages/Coronavirus.aspx");
        urlid.put(7, "https://coronavirus.wa.gov/");
        urlid.put(8, "https://www.cnn.com/");
        urlid.put(9, "https://globalnews.ca/national/videos/");
        urlid.put(10, "https://www.npr.org/sections/news/");
        urlid.put(11, "https://news.sky.com/");
        urlid.put(12, "https://www.indiatoday.in/news.html");
        urlid.put(13, "https://metro.co.uk/news/");
        urlid.put(14, "https://www.rte.ie/news/");
        urlid.put(15, "https://www.euronews.com/");
        urlid.put(16, "https://en.wikipedia.org/wiki/University_of_Pennsylvania");
        urlid.put(17, "https://en.wikipedia.org/wiki/University_of_Michigan");
        urlid.put(18, "https://en.wikipedia.org/wiki/University_of_Chicago");
        urlid.put(19, "https://en.wikipedia.org/wiki/Greece");
        urlid.put(20, "https://en.wikipedia.org/wiki/Doctor_of_Philosophy");


        String[] info1 = new String[]{"Symptoms of Coronavirus | CDC" , "People with COVID-19 have had a wide range of symptoms reported  ranging from mild symptoms to severe illness. Symptoms may appear ..."};
        urlinfo.put("https://www.cdc.gov/coronavirus/2019-ncov/index.html",info1);
        String[] info2 = new String[]{"Reaching ‘Herd Immunity’ Is Unlikely in the U.S., Experts Now Believe" , "Widely circulating coronavirus variants and persistent hesitancy about vaccines will keep the"};
        urlinfo.put("https://www.cdc.gov/coronavirus/2019-ncov/symptoms-testing/symptoms.html",info2);
System.out.println(urlid);
        //static Logger log = Logger.getLogger(searchEngine.class);
        */

      //  HashMap<Integer,String> urlid = new HashMap();
        // HashMap<String,String[]> urlinfo = new HashMap();
        try { 
 
            
               String pathname = "/home/ubuntu/555projectdemo/SearchEngine/src/main/java/queryDataBase/testurl.txt"; 
               File filename = new File(pathname); 
               BufferedReader bf = new BufferedReader(new FileReader(filename));
               String str;
               int i = 0;
               
               while((str=bf.readLine())!=null){
                
                   
                   String[] s = str.split(";");
                   urlid.put(i, s[0]);
                   
                   String[] s2 = Arrays.copyOfRange(s, 1, 3);
                   urlinfo.put(s[0], s2);
                   i++;
                   
                   
       }

       }catch (Exception e) {
           e.printStackTrace();
       }
	}

	public List<String> getUrlList(){
   
		List<String> urls = new ArrayList<String>();

   //for(int i=0; i<20; i++)
   for(int i=0;i<temp.length;i++){
   urls.add(urlid.get(temp[i]));
   }
   
     

  
		return urls;
		
	}
	
	
	public HashMap<String,String[]> getTitlesAndContents(){
  // System.out.println("title: " + urlinfo.get(urlList.get(19))[0]);
		return this.urlinfo;
	
	}
	

	
}
