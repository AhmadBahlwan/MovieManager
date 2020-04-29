package bahlawan.com.mymovies.app.fragments;


import android.content.SharedPreferences;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.List;


import bahlawan.com.mymovies.R;
import bahlawan.com.mymovies.app.Movie;
import bahlawan.com.mymovies.app.MovieRecyclerViewAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;


public class FavouriteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    @BindView(R.id.rvMovies)
    RecyclerView rvMovie;
    MovieRecyclerViewAdapter adapter;
    List<Movie> movies;
    SharedPreferences sharedPreferences;
    int sorting_option_id = 0;

    public FavouriteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = getContext().getSharedPreferences("option id", MODE_PRIVATE);
        sorting_option_id = sharedPreferences.getInt("favourite_fragment", 0);
        Log.d("favourite",String.valueOf(sorting_option_id));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(layoutManager);
        rvMovie.setItemAnimator(new DefaultItemAnimator());
        try{
            loadData();
        }catch (NullPointerException ex){

        }

        rvMovie.setAdapter(adapter);
        return view;
    }

    private void loadData() throws NullPointerException {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Movie>>() {
        }.getType();
        ArrayList<Movie> movies;
        movies = gson.fromJson(json, type);
        if (this.movies == null) {
            this.movies = new ArrayList<>();
        }
        this.movies.addAll(movies);
        adapter = new MovieRecyclerViewAdapter(getContext(), this.movies);
        new Movie().sortMoveisBy(sorting_option_id,movies);
        adapter.notifyDataSetChanged();
        deleteItem();

    }

    private void deleteItem() {
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                movies.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        helper.attachToRecyclerView(rvMovie);
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(movies);
        editor.putString("task list", json);
        editor.apply();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }
}