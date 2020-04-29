package bahlawan.com.mymovies.app.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import bahlawan.com.mymovies.R;
import bahlawan.com.mymovies.app.Movie;
import butterknife.ButterKnife;
import butterknife.BindView;


public class MovieDetailActivity extends AppCompatActivity {

    private static final String EXISITMOVIE = "This movie is already exist";
    Movie movie;
    @BindView(R.id.ivMovieBackdrop)
    ImageView ivMovieBackdrop;
    @BindView(R.id.tvOverview)
    TextView tvOverview;
    List<Movie> fav_movies = new ArrayList<>();
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Movie>>() {}.getType();
        ArrayList<Movie>movies;
        movies = gson.fromJson(json,type);
        if (fav_movies ==null){
            fav_movies =  new ArrayList<>();
        }
        fav_movies.addAll(movies);

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movie = (Movie)extras.getSerializable("CURRENT_MOVIE");
            setTitle(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            Picasso.with(this)
                    .load(movie.getBackdropPath())
                    .into(ivMovieBackdrop);
        }


        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isExisits(movie)){

                    Snackbar.make(view, EXISITMOVIE, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                } else {
                    Snackbar.make(view, "Movie saved as favorite", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    saveData();
                }


            }
        });
    }
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        fav_movies.add(movie);
        String json = gson.toJson(fav_movies);
        editor.putString("task list", json);
        editor.apply();
    }


    public boolean isExisits(Movie movie){
        for (Movie index:fav_movies){
            if (movie.getBackdropPath().equals(index.getBackdropPath()))
                return true;
        }
        return false;
    }
}