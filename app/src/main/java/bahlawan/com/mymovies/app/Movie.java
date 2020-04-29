package bahlawan.com.mymovies.app;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import bahlawan.com.mymovies.R;

public class Movie implements Serializable ,Comparable<Movie> {


    String id;
    String title;
    String overview;
    float popularity;
    float voteAverage;
    float voteCount;
    String posterPath;
    String backdropPath;
    Date release_date;

   public Movie(){

   }

    public Movie(String id, String title, String overview, float popularity, float voteAverage, float voteCount, String posterPath, String backdropPath, Date release_date) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.release_date = release_date;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setVoteCount(float voteCount) {
        this.voteCount = voteCount;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public float getVoteCount() {
        return voteCount;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780%s", backdropPath);
    }

    public float getPopularity() {
        return popularity;
    }

    public Date getRelease_date() {
        return release_date;
    }


    @Override
    public int compareTo(@NonNull Movie o) {
        return 0;
    }

    public static Comparator favorites_compartor = new Comparator<Movie>() {
        public int compare(Movie movie1, Movie movie2) {
            return Float.floatToIntBits(movie2.getVoteCount()) - Float.floatToIntBits(movie1.getVoteCount());
        }
    };
    public static Comparator highest_rated_compartor = new Comparator<Movie>() {
        public int compare(Movie movie1, Movie movie2) {
            return Float.floatToIntBits(movie2.getVoteAverage()) - Float.floatToIntBits(movie1.getVoteAverage());
        }
    };
    public static Comparator most_popular_compartor = new Comparator<Movie>() {
        public int compare(Movie movie1, Movie movie2) {
            return Float.floatToIntBits(movie2.getPopularity()) - Float.floatToIntBits(movie1.getPopularity());
        }
    };
    public static Comparator newest_compartor = new Comparator<Movie>() {
        public int compare(Movie movie1, Movie movie2) {
            return movie2.getRelease_date().compareTo(movie1.getRelease_date());
        }
    };


    public void sortMoveisBy(int sorting_id, List<Movie> movies){

        switch (sorting_id){
            case R.id.highest_rated:
                Collections.sort(movies,highest_rated_compartor);
                break;
            case R.id.favorites:
                Collections.sort(movies,favorites_compartor);
                break;
            case R.id.newest:
                Collections.sort(movies,newest_compartor);
                break;
            case R.id.most_popular:
            default:
                Collections.sort(movies,most_popular_compartor);
        }
    }

}



