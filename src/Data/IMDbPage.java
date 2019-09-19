package Data;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author larik
 */
public class IMDbPage {
    private String type;
    private String url;
    private String name;
    private URL image;
    private String[] genre;
    private Person[] actor;
    private Person director;
    private Person[] creator;
    private String description;
    private String datePublished;
    private String[] keywords;
    //can visit another page with more information breakdown.. do simple for now
    private int ratingCount;
    private float ratingValue;
    private int duration; //in minutes
    private String trailerName;
    private String embedUrl;
    private String thumbnailUrl;
    private String movieDescription;

    
    public IMDbPage(){
        this.type = null;
        this.url = null;
        this.name = null;
        this.image = null;
        this.genre = null;
        this.actor = null;
        this.director = null;
        this.creator = null;
        this.description = null;
        this.datePublished = null;
        this.keywords = null;
        this.ratingCount = -1;
        this.ratingValue = -1;
        this.duration = -1;
        this.trailerName = null;
        this.embedUrl = null;
        this.thumbnailUrl = null;
        this.movieDescription = null;
    }
    
    public IMDbPage(String type, String url, String name, URL image, 
            String[] genre, Person[] actor, Person director, Person[] creator, 
            String description, String datePublished, String[] keywords, 
            int ratingCount, float ratingValue, int duration, String trailerName, 
            String embedUrl, String thumbnailUrl, String movieDescription) {
        this.type = type;
        this.url = url;
        this.name = name;
        this.image = image;
        this.genre = genre;
        this.actor = actor;
        this.director = director;
        this.creator = creator;
        this.description = description;
        this.datePublished = datePublished;
        this.keywords = keywords;
        this.ratingCount = ratingCount;
        this.ratingValue = ratingValue;
        this.duration = duration;
        this.trailerName = trailerName;
        this.embedUrl = embedUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.movieDescription = movieDescription;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(URL image) {
        this.image = image;
    }

    public void setGenre(String[] genre) {
        this.genre = genre;
    }

    public void setActor(Person[] actor) {
        this.actor = actor;
    }

    public void setDirector(Person director) {
        this.director = director;
    }

    public void setCreator(Person[] creator) {
        this.creator = creator;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    @Override
    public String toString() {
        return "IMDbPage{" + "type=" + type + "\n url=" + url + "\n name=" + 
                name + "\n image=" + image + "\n genre=" + Arrays.toString(genre) + "\n actor=" +
                actor + "\n director=" + director + "\n creator=" + creator +
                "\n description=" + description + "\n datePublished=" + 
                datePublished + "\n keywords=" + Arrays.toString(keywords) + "\n ratingCount=" +
                ratingCount + "\n ratingValue=" + ratingValue + "\n duration=" +
                duration + "\n trailerName=" + trailerName + "\n embedUrl=" +
                embedUrl + "\n thumbnailUrl=" + thumbnailUrl +
                "\n movieDescription=" + movieDescription + '}';
    }

    public String getName() {
        return name;
    }
    
    public String getUrl(){
        return url;
    }

    public String getType() {
        return type;
    }

    public URL getImage() {
        return image;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public int getDuration() {
        return duration;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    
    public Person getDirector(){
        return director;
    }
}