package com.example.movieappjava.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosResponse {
    @SerializedName("results")
    private List<Video> videos;

    public List<Video> getVideos() {
        return videos;
    }
}

