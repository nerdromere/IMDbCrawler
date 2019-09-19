/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Data.Person;
import Data.IMDbPage;
import Database.Saver;
import com.mysql.jdbc.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 *
 * @author larik
 */
public class IMDbCrawler {
    private static final String MOVIE = "https://www.imdb.com/title/tt0993846/";
    private static final String BASE_URI = "https://www.imdb.com";
    private ArrayList<IMDbPage> movies = new ArrayList<>();
    private Connection c;
    private static Saver save = null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //get initial document of initial movie-Shawshank
            Document doc = Jsoup.connect(MOVIE).get();
            save = new Saver("localhost", "imdb", "root", "admin");
            save.savePage(createIMDbPage(doc));
            visitSaveRecursively(doc);
        } catch (IOException ex) {
            System.out.println("Input output did not work");
        }
    }
    
    static void visitSaveRecursively(Document root) throws IOException {
        ArrayList<Document> recPages = recommendedMovies(root);
        //save recommended movies
        for(Document d : recPages){
            save.savePage(createIMDbPage(d));
        }
        /*go to each child that is not in the databse, it will then leave this
          current call and go up the call stack*/
        for(Document d : recPages){
            visitSaveRecursively(d);
        }
        System.out.println(createIMDbPage(root).getName());
    }
    
    /**
     * Next improvement, don't CONNECT to the pages you don't need 
     * to in recommended movies by looking at the urls! Switch to arraylist!
     * @param doc
     * @return
     * @throws IOException 
     */
    static ArrayList<Document> recommendedMovies(Document doc) throws IOException{
        Elements titleRecs = doc.select("div.rec_item");
        Elements links = titleRecs.select("a[href*=title]");
        ArrayList<Document> recommendedPages = new ArrayList();
        for (int i = 0; i < links.size(); i++) {
            //System.out.println(links.get(i).attr("href").substring(0, links.get(i).attr("href").indexOf('?')));
            if(!save.pageExists(links.get(i).attr("href").substring(0, links.get(i).attr("href").indexOf('?'))))
                recommendedPages.add(Jsoup.connect(BASE_URI + links.get(i).attr("href")).get());
        }
        System.out.println(recommendedPages.size());
        return recommendedPages;
    }
    
    static IMDbPage createIMDbPage(Document doc){
        IMDbPage page = new IMDbPage();
        try {
            Element json = doc.select("script[type='application/ld+json']").first();
            JSONObject jo = createJsonObject(json.data());
            page.setType((String)jo.get("@type"));
            page.setUrl((String)jo.get("url"));
            page.setName((String) jo.get("name"));
            if(jo.containsKey("image"))
                page.setImage(new URL((String) jo.get("image")));
            page.setGenre(setGenre(jo));
            //Add Actors
            page.setDirector(getDirector(jo));
            //Add Creators
            page.setDescription((String) jo.get("description"));
            page.setDatePublished((String) jo.get("datePublished"));
            //Add keywords
            setRating(page, jo);
            page.setDuration(setDuration(jo));
            setTrailerInfo(page, jo);
            page.setThumbnailUrl((String) jo.get("thumbnailUrl"));
            page.setMovieDescription((String) jo.get("description"));
        } catch(IOException ex) {
            System.out.println("Website not found!");
                }
        return page;
    }
    
    private static String[] setGenre(JSONObject jo){
        if(!jo.containsKey("genre"))
            return null;
        JSONArray genres = null;
        String[] arr = null;
        try{ //try if it is an array, if not, catch and parse JSONObject
            genres = (JSONArray) jo.get("genre");
            arr = new String[genres.size()];
            Iterator outerIt = genres.iterator();
            for (int i = 0; i < genres.size(); i++) {
                arr[i] = (String)outerIt.next();
            }
        } catch(ClassCastException ex){ //if it is not an array, it is just an object
            arr = new String[1];
            arr[0] = (String) jo.get("genre");
        }
        return arr;
    }
    
    private static boolean setRating(IMDbPage page, JSONObject jo){
        if(!jo.containsKey("aggregateRating"))
            return false;
        Map aggregateRating = (Map) jo.get("aggregateRating");
        page.setRatingCount(Integer.parseInt(aggregateRating.get("ratingCount").toString()));
        page.setRatingValue(Float.parseFloat(aggregateRating.get("ratingValue").toString()));
        return true;
    }
    
    private static int setDuration(JSONObject jo){
        if(!jo.containsKey("duration")){
            return -1;
        }
        int total = 0;
        String time = (String)jo.get("duration");
        if(time.contains("H")){
            total += 60 * Integer.parseInt(time.substring(time.indexOf("T") + 1, time.indexOf("H")));
            if(time.contains("M"))
                total += Integer.parseInt(time.substring(time.indexOf("H") + 1, time.indexOf("M")));
        } else {
            total += Double.parseDouble(time.substring(time.indexOf("T") + 1, time.indexOf("M")));
        }
        return total;
    }
    
    private static JSONObject createJsonObject(String json)
    {
        JSONObject jo = null;
        try{
            //Read from file
            Object obj = new JSONParser().parse(json);
            //Cast to JSONObject
            jo = (JSONObject)obj;
        } catch(ParseException e) {
            System.out.println("Could not parse file");
        } catch(ClassCastException e) {
            System.out.println("Could not cast object");
        }
        return jo;
    }
    
    /**
     * Set the trailer name and url of trailer
     * @param page  page to add the trailer info
     * @param jo    JSONObject from where to read from
     */
    private static boolean setTrailerInfo(IMDbPage page, JSONObject jo){
        if(!jo.containsKey("trailer"))
            return false;
        Map trailer = (Map)jo.get("trailer");
        Iterator<Map.Entry> itr = trailer.entrySet().iterator();
        itr.next(); //type
        page.setTrailerName(itr.next().getValue().toString());
        page.setEmbedUrl(itr.next().getValue().toString());
        return true;
    }
    
    /**
     * Method which parses the director out of the JSONObject and returns a
     * Person object. 
     * 
     * TODO: Create this to be an array; currently only takes in one or first
     *       director.
     * 
     * We are assuming this is a person type for now. If this begins to cause 
     * trouble, we can change a few objects around such as having a Type object
     * and defining it a person or organization. 
     * 
     * @param jo    JSONObject of the IMDBPage
     * @return      The person object
     */
    private static Person getDirector(JSONObject jo){
        if(!jo.containsKey("director")){
            return null;
        }
        Person person = new Person();
        //System.out.println(jo);
        try{
            Map director = (Map) jo.get("director");
            Iterator<Map.Entry> itr = director.entrySet().iterator();
            itr.next(); //Skip Type
            person.setName(itr.next().getValue().toString()); //Name of person
            person.setUrl(itr.next().getValue().toString()); //URL of person
        } catch(ClassCastException ex){ //try as an array
            //create json array
            JSONArray directors = (JSONArray) jo.get("director");
            //grab its iterator
            Iterator outerIt = directors.iterator();
            //create the json object on first director
            JSONObject temp = (JSONObject) outerIt.next();
            //System.out.println(temp);
            person.setName((String)temp.get("name"));
            person.setUrl((String)temp.get("url"));
        }
        return person;
    }
}
