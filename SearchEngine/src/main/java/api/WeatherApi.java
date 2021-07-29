package api;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import LRUCacheQueries.LRUCache;
import SearchEngine.searchEngine;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class WeatherApi {
    static Logger log = Logger.getLogger(WeatherApi.class);
    

    private static final String WEATHER_API = "http://api.openweathermap.org/data/2.5/weather?units=metric&appid=c8e43a26d625ce8e89db25631fa4e3e0&mode=xml&q=";
    
    
    private static final String FORECAST_API = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=xml&units=metric&appid=c8e43a26d625ce8e89db25631fa4e3e0&q=";

    private static LRUCache<weatherinfo> currentCache = new LRUCache<>(10);
    
    
    private static LRUCache<weatherinfo[]> forecastCache = new LRUCache<>(10);

    public WeatherApi() {    	
    }



    public static weatherinfo searchCurrent(String keywords) {
        weatherinfo weatherInfo = currentCache.get(keywords);
        
        
        if (weatherInfo != null) {
        
        
            return weatherInfo;
        }
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            
            Document doc = db.parse(WEATHER_API + keywords);

            weatherInfo = new weatherinfo();

 
 
 
            Node node = doc.getElementsByTagName("city").item(0);
            if (node == null) {
            
            
                log.warn("city is null");
                
                
                return null;
            }
            weatherInfo.state = node.getAttributes().getNamedItem("name").getNodeValue() + ", " + doc.getElementsByTagName("country").item(0).getTextContent();

            
            
            
            node = doc.getElementsByTagName("temperature").item(0);
            if (node == null) {
            
            
                log.warn("temperature is null");
                return null;
                
                
            }
            weatherInfo.value = node.getAttributes().getNamedItem("value").getNodeValue();
            weatherInfo.min = node.getAttributes().getNamedItem("min").getNodeValue();
            
            
            weatherInfo.max = node.getAttributes().getNamedItem("max").getNodeValue();

            
            
            
            node = doc.getElementsByTagName("humidity").item(0);
            if (node == null) {
            
            
                log.warn("humidity is null");
                
                
                return null;
            }
            weatherInfo.humidity = node.getAttributes().getNamedItem("value").getNodeValue() + node.getAttributes().getNamedItem("unit").getNodeValue();

           
           
           
            node = doc.getElementsByTagName("speed").item(0);
            if (node == null) {
                log.warn("speed is null");
                
                
                return null;
            }
            weatherInfo.wind = node.getAttributes().getNamedItem("name").getNodeValue();

           
           
           
            node = doc.getElementsByTagName("weather").item(0);
            if (node == null) {
            
            
                log.warn("weather is null");
                return null;
            }
            weatherInfo.weather = node.getAttributes().getNamedItem("value").getNodeValue();
            
            
            weatherInfo.icon = "http://openweathermap.org/img/w/" + node.getAttributes().getNamedItem("icon").getNodeValue() + ".png";

            
            
            
            node = doc.getElementsByTagName("lastupdate").item(0);
            if (node == null) {
                log.warn("time is null");
                
                
                return null;
            }
            weatherInfo.time = node.getAttributes().getNamedItem("value").getNodeValue().replace("T", " ");
            currentCache.put(keywords, weatherInfo);

            return weatherInfo;
        } catch (Exception e) {
            e.printStackTrace();
            
            
            return null;
        }
    }



    public static weatherinfo[] searchForcast(String keywords) {
    	weatherinfo[] infos = forecastCache.get(keywords);
        if (infos != null) {
            return infos;
        }
        infos = new weatherinfo[7];
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            
            Document doc = db.parse(FORECAST_API + keywords);
           
            
            String state = doc.getElementsByTagName("name").item(0).getTextContent() + ", " + doc.getElementsByTagName("country").item(0).getTextContent();

       
       
            for (int i = 0; i < 7; i++) {
            	weatherinfo weatherInfo = new weatherinfo();

              
              
                weatherInfo.state = state;

               
               
                weatherInfo.value = doc.getElementsByTagName("temperature").item(i).getAttributes().getNamedItem("day").getNodeValue();
                
                weatherInfo.min = doc.getElementsByTagName("temperature").item(i).getAttributes().getNamedItem("min").getNodeValue();
                
                weatherInfo.max = doc.getElementsByTagName("temperature").item(i).getAttributes().getNamedItem("max").getNodeValue();
                

                
                
                weatherInfo.humidity = doc.getElementsByTagName("humidity").item(i).getAttributes().getNamedItem("value").getNodeValue() + doc.getElementsByTagName("humidity").item(i).getAttributes().getNamedItem("unit").getNodeValue();

                
                
                weatherInfo.wind = doc.getElementsByTagName("windSpeed").item(i).getAttributes().getNamedItem("name").getNodeValue();

                
                
                weatherInfo.weather = doc.getElementsByTagName("symbol").item(i).getAttributes().getNamedItem("name").getNodeValue();
                weatherInfo.icon = "http://openweathermap.org/img/w/" + doc.getElementsByTagName("symbol").item(i).getAttributes().getNamedItem("var").getNodeValue() + ".png";

               
               
                weatherInfo.time = doc.getElementsByTagName("time").item(i).getAttributes().getNamedItem("day").getNodeValue();

                infos[i] = weatherInfo;
            }
            forecastCache.put(keywords, infos);
            
            return infos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) {
    	WeatherApi weather = new WeatherApi();
     
    	weatherinfo info = searchCurrent("philadelphia");
    	System.out.println(info.state);


    	
    }
}
