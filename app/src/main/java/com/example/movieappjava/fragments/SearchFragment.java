package com.example.movieappjava.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieappjava.MovieDetailsActivity;
import com.example.movieappjava.R;
import com.example.movieappjava.api.MovieApiService;
import com.example.movieappjava.models.Movie;
import com.example.movieappjava.models.MovieResponse;
import com.example.movieappjava.refrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {
    private GridLayout gridLayout;
    private MovieApiService movieApiService;
    private static final String API_KEY = "879aca7946aa41e4984e98ebbad614c5";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        gridLayout = view.findViewById(R.id.gridLayoutSearch);
        EditText searchInput = view.findViewById(R.id.editTextSearch);

        movieApiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    searchMovies(s.toString());
                } else {
                    gridLayout.removeAllViews();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void searchMovies(String query) {
        Call<MovieResponse> call = movieApiService.searchMovies(API_KEY, query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayMovies(response.body().getMovies());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load movies", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayMovies(List<Movie> movies) {
        gridLayout.removeAllViews();

        int totalWidth = gridLayout.getWidth();
        int columnCount = gridLayout.getColumnCount();
        int imageSize = totalWidth / columnCount - 16;

        for (Movie movie : movies) {
            ImageView posterView = new ImageView(getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = imageSize;
            params.height = imageSize;
            params.setMargins(8, 8, 8, 8);
            posterView.setLayoutParams(params);

            String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
            Glide.with(getContext()).load(posterUrl).into(posterView);
            posterView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            posterView.setOnClickListener(v -> onMovieClick(movie));

            gridLayout.addView(posterView);
        }
    }

    private void onMovieClick(Movie movie) {
        Intent intent = new Intent(requireContext(), MovieDetailsActivity.class);
        intent.putExtra("MOVIE_ID", movie.getId());
        startActivity(intent);
    }
}