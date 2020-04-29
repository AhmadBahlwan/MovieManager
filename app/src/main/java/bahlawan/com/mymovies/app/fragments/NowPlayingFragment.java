package bahlawan.com.mymovies.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bahlawan.com.mymovies.R;
import bahlawan.com.mymovies.app.Movie;
import bahlawan.com.mymovies.app.MovieLoader;
import bahlawan.com.mymovies.app.MovieRecyclerViewAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;


public class NowPlayingFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Movie>>{

    private static final String NOW_PLAYING_REQUEST_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private List<Movie> movies;
    MovieRecyclerViewAdapter adapter;
    @BindView(R.id.rvMovies)
    RecyclerView rvMovies;
    int sorting_option_id = 0;
    SharedPreferences sharedPreferences;

    public NowPlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        movies = new ArrayList<>();
        adapter = new MovieRecyclerViewAdapter(getContext(),movies);
        ButterKnife.bind(this, view);
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        sharedPreferences = getContext().getSharedPreferences("option id", Context.MODE_PRIVATE);
        sorting_option_id = sharedPreferences.getInt("now_playing_fragment", 0);
        Log.d("now",String.valueOf(sorting_option_id));
        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(llm);
        rvMovies.setAdapter(adapter);

        ConnectivityManager connMgr = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected())
            getLoaderManager().initLoader(0,null,this);
        else {
            Toast.makeText(getContext(),getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }

        return view;
    }



    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        MovieLoader loader = new MovieLoader(getContext(),NOW_PLAYING_REQUEST_URL);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        movies.clear();
        if (this.movies!=null && data!=null){
            movies.addAll(data);
            new Movie().sortMoveisBy(sorting_option_id,movies);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
        movies.clear();
    }

}
