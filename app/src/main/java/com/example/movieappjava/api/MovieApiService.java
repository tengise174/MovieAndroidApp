package com.example.movieappjava.api;

import com.example.movieappjava.models.CreditsResponse;
import com.example.movieappjava.models.Movie;
import com.example.movieappjava.models.MovieResponse;
import com.example.movieappjava.models.ReviewsResponse;
import com.example.movieappjava.models.VideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {
    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MovieResponse> searchMovies(@Query("api_key") String apiKey, @Query("query") String query);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(@Path("movie_id") String movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/credits")
    Call<CreditsResponse> getMovieCredits(@Path("movie_id") String movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getMovieReviews(@Path("movie_id") String movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<VideosResponse> getMovieVideos(@Path("movie_id") String movieId, @Query("api_key") String apiKey);


}
