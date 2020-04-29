package bahlawan.com.mymovies.app;


import android.content.Context;

import java.util.List;


public class MovieLoader extends android.support.v4.content.AsyncTaskLoader<List<Movie>> {

    private String url;

    public MovieLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public List<Movie> loadInBackground() {
        return loadMovies();
    }
    public List<Movie> loadMovies(){

        List<Movie> movies;
        if (url == null){
            return null;
        }
        movies = Utils.fetchMovieData(url);
        return movies;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
