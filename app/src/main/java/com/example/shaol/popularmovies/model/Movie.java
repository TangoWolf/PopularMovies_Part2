package com.example.shaol.popularmovies.model;

/**
 * Created by shaol on 4/25/2018.
 */

public class Movie {
    private int id;
    private String rating;
    private String title;
    private String image;
    private String synopsis;
    private String releaseDate;

    public Movie(int id, String title, String image, String rating, String synopsis, String releaseDate) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getImage() {return image;}

    public void setImage(String image) {this.image = image;}

    public String getRating() {return rating;}

    public void setRating(String rating) {this.rating = rating;}

    public String getSynopsis() {return synopsis;}

    public void setSynopsis(String synopsis) {this.synopsis = synopsis;}

    public String getReleaseDate() {return releaseDate;}

    public void setReleaseDate(String releaseDate) {this.releaseDate = releaseDate;}
}
