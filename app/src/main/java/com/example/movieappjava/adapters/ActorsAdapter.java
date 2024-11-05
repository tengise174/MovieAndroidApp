package com.example.movieappjava.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieappjava.R;
import com.example.movieappjava.models.Cast;
import java.util.List;

public class ActorsAdapter extends RecyclerView.Adapter<ActorsAdapter.ActorViewHolder> {
    private List<Cast> actorList;

    public ActorsAdapter(List<Cast> actorList) {
        this.actorList = actorList;
    }

    @NonNull
    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actor, parent, false);
        return new ActorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorViewHolder holder, int position) {
        Cast actor = actorList.get(position);
        holder.bind(actor);
    }

    @Override
    public int getItemCount() {
        return actorList.size();
    }

    class ActorViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewActor;
        private TextView textViewActorName;

        public ActorViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewActor = itemView.findViewById(R.id.imageViewActor);
            textViewActorName = itemView.findViewById(R.id.textViewActorName);
        }

        public void bind(Cast actor) {
            String profilePath = "https://image.tmdb.org/t/p/w500" + actor.getProfilePath();
            Glide.with(itemView.getContext())
                    .load(profilePath)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewActor);
            textViewActorName.setText(actor.getName());
        }
    }
}

