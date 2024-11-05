package com.example.movieappjava.models;

import com.google.gson.annotations.SerializedName;

public class Cast {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_path")
    private String profilePath;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath; }
}
