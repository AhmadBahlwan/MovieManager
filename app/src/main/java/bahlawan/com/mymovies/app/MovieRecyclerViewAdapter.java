package bahlawan.com.mymovies.app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import bahlawan.com.mymovies.R;
import bahlawan.com.mymovies.app.activities.MovieDetailActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {


    List<Movie> movies;
    Context context;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);

        return new ViewHolder(v);
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.tvTile.setText(movie.getTitle());
        holder.tvOverView.setText(movie.getOverview());
        holder.tvVote_Average.setText("Vote:" + movie.getVoteAverage());
        Picasso.with(getContext())
                .load(movie.getPosterPath())
                .into(holder.ivMovieImage);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ivMovieImage)
        public ImageView ivMovieImage;
        @BindView(R.id.tvTitle)
        public TextView tvTile;
        @BindView(R.id.cvMovie)
        CardView cvMovie;
        @BindView(R.id.tvOverview)
        public TextView tvOverView;
        @BindView(R.id.vote_average)
        public TextView tvVote_Average;

        ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movie movie = movies.get(getAdapterPosition());
            Intent intent = new Intent(getContext(), MovieDetailActivity.class);
            intent.putExtra("CURRENT_MOVIE",movie);
            getContext().startActivity(intent);
        }
    }
}
