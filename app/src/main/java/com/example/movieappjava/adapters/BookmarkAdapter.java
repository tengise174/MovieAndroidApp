package com.example.movieappjava.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.movieappjava.R;
import com.example.movieappjava.models.Movie;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {
    private final List<Movie> bookmarks;

    private final OnBookmarkClickListener listener;

    public interface OnBookmarkClickListener {
        void onBookmarkClick(Movie movie);
    }

    public BookmarkAdapter(List<Movie> bookmarks, OnBookmarkClickListener listener) {
        this.bookmarks = bookmarks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        Movie movie = bookmarks.get(position);
        holder.bind(movie);

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = 450;
        holder.itemView.setLayoutParams(layoutParams);

        holder.itemView.setOnClickListener(v -> listener.onBookmarkClick(movie));
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;
        TextView titleTextView;
        TextView genreTextView;
        TextView releaseDateTextView;
        TextView runtimeTextView;
        TextView voteTextView;

        BookmarkViewHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.bookmark_poster);
            titleTextView = itemView.findViewById(R.id.bookmark_title);
            genreTextView = itemView.findViewById(R.id.bookmark_genre);
            releaseDateTextView = itemView.findViewById(R.id.bookmark_ReleaseDate);
            runtimeTextView = itemView.findViewById(R.id.bookmark_runtime);
            voteTextView = itemView.findViewById(R.id.bookmark_vote);
        }

        void bind(Movie movie) {
            titleTextView.setText(movie.getTitle());
            genreTextView.setText(movie.getFirstGenreName().isEmpty() ? "Unknown" : movie.getFirstGenreName());
            releaseDateTextView.setText(movie.getReleaseDate());
            runtimeTextView.setText(movie.getRuntime() + " min");
            voteTextView.setText(String.valueOf(movie.getVote_average()));
            String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();

            Glide.with(itemView.getContext())
                    .load(posterUrl)
                    .into(posterImageView);
        }
        }
    }


