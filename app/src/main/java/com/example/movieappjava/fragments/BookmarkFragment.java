package com.example.movieappjava.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieappjava.MovieDetailsActivity;
import com.example.movieappjava.R;
import com.example.movieappjava.bookmark.BookMarkManager;
import com.example.movieappjava.adapters.BookmarkAdapter;
import com.example.movieappjava.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class BookmarkFragment extends Fragment implements BookmarkAdapter.OnBookmarkClickListener {
    private RecyclerView recyclerView;
    private BookmarkAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Movie> bookmarkedMovies = new ArrayList<>(BookMarkManager.getInstance().getBookmarks());
        adapter = new BookmarkAdapter(bookmarkedMovies, this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onBookmarkClick(Movie movie) {
        Intent intent = new Intent(requireContext(), MovieDetailsActivity.class);
        intent.putExtra("MOVIE_ID", movie.getId());
        startActivity(intent);
    }
}