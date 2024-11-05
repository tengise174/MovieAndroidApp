package com.example.movieappjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieappjava.adapters.ReviewsAdapter;
import com.example.movieappjava.api.MovieApiService;
import com.example.movieappjava.bookmark.BookMarkManager;
import com.example.movieappjava.adapters.ActorsAdapter;
import com.example.movieappjava.models.Cast;
import com.example.movieappjava.models.CreditsResponse;
import com.example.movieappjava.models.Movie;
import com.example.movieappjava.models.Review;
import com.example.movieappjava.models.ReviewsResponse;
import com.example.movieappjava.models.Video;
import com.example.movieappjava.models.VideosResponse;
import com.example.movieappjava.refrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String API_KEY = "879aca7946aa41e4984e98ebbad614c5";
    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView releaseDateTextView;
    private TextView overviewTextView;
    private String movieId;
    private TextView runtime;
    private TextView genre;
    private TextView vote_average;
    private ImageView bookmarkButton;
    private Movie movie;
    private ImageView returnButton;
    private RecyclerView actorsRecyclerView;
    private ActorsAdapter actorsAdapter;
    private RecyclerView reviewsRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private List<Cast> actorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        posterImageView = findViewById(R.id.imageViewPoster);
        titleTextView = findViewById(R.id.textViewTitle);
        releaseDateTextView = findViewById(R.id.textViewReleaseDate);
        overviewTextView = findViewById(R.id.textViewOverview);
        runtime = findViewById(R.id.runtime);
        genre = findViewById(R.id.genre);
        vote_average = findViewById(R.id.vote_average);
        bookmarkButton = findViewById(R.id.buttonBookmark);
        returnButton = findViewById(R.id.buttonReturn);
        reviewsRecyclerView = findViewById(R.id.recyclerViewReviews);
        reviewsAdapter = new ReviewsAdapter();
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        movieId = getIntent().getStringExtra("MOVIE_ID");

        fetchMovieDetails(movieId);
        fetchReviews(movieId);

        bookmarkButton.setOnClickListener(view -> {
            if (BookMarkManager.getInstance().isBookmarked(movieId)) {
                BookMarkManager.getInstance().removeBookmark(movieId);
                bookmarkButton.setImageResource(R.drawable.ic_bookmark_empty);
                Toast.makeText(this, "Removed from bookmarks", Toast.LENGTH_SHORT).show();
            } else {
                BookMarkManager.getInstance().addBookmark(movie);
                bookmarkButton.setImageResource(R.drawable.ic_bookmark_full);
                Toast.makeText(this, "Added to bookmarks", Toast.LENGTH_SHORT).show();
            }
        });

        returnButton.setOnClickListener(view -> finish());
    }

    private void fetchMovieDetails(String movieId) {
        if (movieId == null) {
            Toast.makeText(this, "Invalid movie ID", Toast.LENGTH_SHORT).show();
            return;
        }

        MovieApiService movieApiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);
        Call<Movie> call = movieApiService.getMovieDetails(movieId, API_KEY);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movie = response.body();
                    displayMovieDetails(movie);
                    fetchActors(movieId);
                    fetchMovieVideos(movieId);
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "Movie details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MovieDetailsActivity.this, "Failed to load movie details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMovieVideos(String movieId) {
        MovieApiService movieApiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);
        Call<VideosResponse> call = movieApiService.getMovieVideos(movieId, API_KEY);
        call.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Video> videos = response.body().getVideos();
                    if (!videos.isEmpty()) {
                        String videoKey = videos.get(0).getKey();
                        setupTrailerButton(videoKey);
                    }
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "No videos found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                Toast.makeText(MovieDetailsActivity.this, "Failed to load videos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupTrailerButton(String videoKey) {
        Button buttonPlayTrailer = findViewById(R.id.buttonPlayTrailer);
        buttonPlayTrailer.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoKey));
            startActivity(intent);
        });
    }

    private void fetchActors(String movieId) {
        MovieApiService movieApiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);
        Call<CreditsResponse> call = movieApiService.getMovieCredits(movieId, API_KEY);

        call.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    actorList.clear();
                    actorList.addAll(response.body().getCast());
                    setupActorRecyclerView();
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "No actors found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CreditsResponse> call, Throwable t) {
                Toast.makeText(MovieDetailsActivity.this, "Failed to load actors", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchReviews(String movieId) {
        MovieApiService movieApiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);
        Call<ReviewsResponse> call = movieApiService.getMovieReviews(movieId, API_KEY);

        call.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Review> reviews = response.body().getReviews();
                    reviewsAdapter.setReviewList(reviews);
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "No reviews found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Toast.makeText(MovieDetailsActivity.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupActorRecyclerView() {
        RecyclerView recyclerViewActors = findViewById(R.id.recyclerViewActors);
        recyclerViewActors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ActorsAdapter actorAdapter = new ActorsAdapter(actorList);
        recyclerViewActors.setAdapter(actorAdapter);
    }

    private void displayMovieDetails(Movie movie) {
        titleTextView.setText(movie.getTitle());
        releaseDateTextView.setText(movie.getReleaseDate());
        overviewTextView.setText(movie.getOverview());
        runtime.setText(movie.getRuntime() + " minute");
        genre.setText(movie.getFirstGenreName());
        vote_average.setText(Float.toString(movie.getVote_average()));

        String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Glide.with(this).load(posterUrl).into(posterImageView);

        updateBookmarkIcon(movieId);
    }

    private void updateBookmarkIcon(String movieId) {
        if (BookMarkManager.getInstance().isBookmarked(movieId)) {
            bookmarkButton.setImageResource(R.drawable.ic_bookmark_full);
        } else {
            bookmarkButton.setImageResource(R.drawable.ic_bookmark_empty);
        }
    }
}