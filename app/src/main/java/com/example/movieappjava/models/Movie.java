package com.example.movieappjava.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate; // Add this field

    @SerializedName("overview")
    private String overview; // Add this field

    @SerializedName("runtime")
    private String runtime;

    @SerializedName("genres")
    private JsonArray genres;

    @SerializedName("vote_average")
    private float vote_average;

    public String getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getFirstGenreName() {
        if (genres != null && genres.size() > 0) {
            JsonObject firstGenre = genres.get(0).getAsJsonObject();
            return firstGenre.get("name").getAsString();
        }
        return null;
    }
    public float getVote_average() {
        return vote_average;
    }
}
