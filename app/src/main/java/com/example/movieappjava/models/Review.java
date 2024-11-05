package com.example.movieappjava.models;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
