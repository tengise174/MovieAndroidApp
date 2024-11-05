package com.example.movieappjava.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class HomeFragment extends Fragment {
    private GridLayout gridLayout;
    private MovieApiService movieApiService;
    private static final String API_KEY = "879aca7946aa41e4984e98ebbad614c5";
    private LinearLayout topRatedMoviesLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        gridLayout = view.findViewById(R.id.gridLayoutMovies);
        topRatedMoviesLayout = view.findViewById(R.id.topRatedMoviesLayout);
        movieApiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);

        Button nowPlayingButton = view.findViewById(R.id.nowPlayingButton);
        Button topRatedButton = view.findViewById(R.id.topRatedButton);
        Button upcomingButton = view.findViewById(R.id.upcomingButton);
        Button popularButton = view.findViewById(R.id.popularButton);

        fetchMovies("now_playing");
        fetchTopRatedMovies();

        nowPlayingButton.setOnClickListener(v -> fetchMovies("now_playing"));
        topRatedButton.setOnClickListener(v -> fetchMovies("top_rated"));
        upcomingButton.setOnClickListener(v -> fetchMovies("upcoming"));
        popularButton.setOnClickListener(v -> fetchMovies("popular"));

        return view;
    }

    private void fetchTopRatedMovies() {
        Call<MovieResponse> call = movieApiService.getTopRatedMovies(API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayTopRatedMovies(response.body().getMovies());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load top rated movies", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTopRatedMovies(List<Movie> movies) {
        topRatedMoviesLayout.removeAllViews();

        int posterWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.3);
        int posterHeight = (int) (posterWidth * 1.5);

        for (int i = 0; i < Math.min(5, movies.size()); i++) {
            Movie movie = movies.get(i);
            ImageView posterView = new ImageView(getContext());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(posterWidth, posterHeight);
            params.setMargins(8, 8, 8, 8);
            posterView.setLayoutParams(params);


            String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
            Glide.with(getContext()).load(posterUrl).into(posterView);
            posterView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            posterView.setOnClickListener(v -> onMovieClick(movie));
            topRatedMoviesLayout.addView(posterView);
        }
    }

    private void fetchMovies(String category) {
        Call<MovieResponse> call;
        switch (category) {
            case "now_playing":
                call = movieApiService.getNowPlayingMovies(API_KEY);
                break;
            case "top_rated":
                call = movieApiService.getTopRatedMovies(API_KEY);
                break;
            case "upcoming":
                call = movieApiService.getUpcomingMovies(API_KEY);
                break;
            case "popular":
                call = movieApiService.getPopularMovies(API_KEY);
                break;
            default:
                return;
        }

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

        for (int i = 0; i < Math.min(15, movies.size()); i++) {
            Movie movie = movies.get(i);
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