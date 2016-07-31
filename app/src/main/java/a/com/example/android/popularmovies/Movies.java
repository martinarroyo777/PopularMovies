package a.com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by martinarroyo on 7/22/16.
 *
 * A custom class to hold movie information obtained from the tmdb.org API. Each Movies object will
 * hold the following information for a movie:
 *
 * - The complete path to that movie's poster: the constant POSTER_BASE_URL + "posterPath" (String)
 * - The movie title: "title" (String)
 * - An overview of the movie: "synopsis" (String)
 * - The movie's user rating: "rating" (Double)
 * - The movie's release date: "releaseDate" (String)
 */
public class Movies implements Parcelable{
    // Fields
    private String posterPath;
    private String title;
    private String synopsis;
    private Double rating;
    private String releaseDate;
    private String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    // Constructors
    public Movies(){
    }

    public Movies(String posterPath, String title, String synopsis, Double rating, String releaseDate){
        this.posterPath = POSTER_BASE_URL + posterPath;
        this.title = title;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }
    // Constructor for Parcelable
    protected Movies(Parcel parcel){
        this.posterPath = parcel.readString();
        this.title = parcel.readString();
        this.synopsis = parcel.readString();
        this.rating = parcel.readDouble();
        this.releaseDate = parcel.readString();
    }
    // Used for Parcelable
    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags){
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(synopsis);
        dest.writeDouble(rating);
        dest.writeString(releaseDate);
    }

    // Mutators
    public void setPosterPath(String posterPath){
        this.posterPath = POSTER_BASE_URL + posterPath;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setSynopsis(String synopsis){
        this.synopsis = synopsis;
    }
    public void setRating(Double rating){
        this.rating = rating;
    }
    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }

    // Accessors
    public String getPosterPath(){
        return this.posterPath;
    }
    public String getTitle(){
        return this.title;
    }
    public String getSynopsis(){
        return this.synopsis;
    }
    public Double getRating(){
        return this.rating;
    }
    public String getReleaseDate(){
        return this.releaseDate;
    }

    public String toString(){
        return "Poster Path: " + this.posterPath + "\n" +
                "Title: " + this.title + "\n" +
                "Synopsis: " + this.synopsis + "\n" +
                "Rating: " + Double.toString(this.rating) + "\n" +
                "Release Date: " + this.releaseDate + "\n";
    }

    // Creator for Parcelable
    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>(){
        @Override
        public Movies createFromParcel(Parcel in){
            return new Movies(in);
        }
        @Override
        public Movies [] newArray(int size){
            return new Movies[size];
        }
    };
}
