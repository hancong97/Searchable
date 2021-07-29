package test;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class sendRequest implements Runnable{

	private String url; 
 
 
	public sendRequest(String url) {


		this.url = url;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
			Document doc = null;
		try {

			doc = Jsoup.connect(url).get();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		String title = doc.title();
		Elements metas = doc.head().select("meta");
   
   
   
		for (Element meta : metas) {  
   
			String content = meta.attr("content");  
			if ("keywords".equalsIgnoreCase(meta.attr("name"))) {  
      
      
                System.out.println(content);  
            } 
            
            
            
            
			if ("description".equalsIgnoreCase(meta.attr("name"))) {  
                System.out.println(content);  
            }  
			
		}
   
		Elements keywords = doc.getElementsByTag("meta");
        System.out.println(title);
		
	}

}
