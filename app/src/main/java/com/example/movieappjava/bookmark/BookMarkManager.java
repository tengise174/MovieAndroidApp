package com.example.movieappjava.bookmark;

import com.example.movieappjava.models.Movie;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BookMarkManager {
    private static BookMarkManager instance;
    private final Map<String, Movie> bookmarks = new HashMap<>();

    private BookMarkManager() {}

    public static BookMarkManager getInstance() {
        if (instance == null) {
            instance = new BookMarkManager();
        }
        return instance;
    }

    public void addBookmark(Movie movie) {
        bookmarks.put(movie.getId(), movie);
    }

    public void removeBookmark(String movieId) {
        bookmarks.remove(movieId);
    }

    public boolean isBookmarked(String movieId) {
        return bookmarks.containsKey(movieId);
    }

    public Collection<Movie> getBookmarks() {
        return bookmarks.values();
    }
}

