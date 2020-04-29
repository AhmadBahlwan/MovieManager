package bahlawan.com.mymovies.app;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import bahlawan.com.mymovies.R;
import bahlawan.com.mymovies.app.activities.MovieDetailActivity;

public class Connection extends JobService {
    private static final String NOW_PLAYING_REQUEST_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private static final String TAG = "JobService";
    private boolean jobCancelled = false;
    List<Movie>movies;

    @Nullable
    private void createNotification(Movie movie) {
        Intent intent;
        URL url = null;

            try {
                url = new URL(movie.getBackdropPath());
            } catch (MalformedURLException ex) {

            }


        Bitmap img = null;
        try {
            img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException ex) {
        }

        intent = new Intent(this, MovieDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("CURRENT_MOVIE", movie);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_movie_creation_black_24dp)
                .setLargeIcon(img)
                .setContentIntent(pendingIntent)
                .setContentTitle(movie.getTitle())
                .setContentText("Tap to read movie story...")
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(img).bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat.from(this.getApplicationContext()).notify(1, builder.build());

    }

    private void doBackgroundWork(final JobParameters parms) {
        (new Thread(new Runnable() {
            public void run() {
                try {

                    Thread.sleep(10000L);
                } catch (InterruptedException ex) {

                }
                Random random = new Random();
                Connection.this.movies = (new MovieLoader(Connection.this.getApplicationContext(), NOW_PLAYING_REQUEST_URL  )).loadMovies();
                int index = random.nextInt(Connection.this.movies.size());
                createNotification(movies.get(index));
                Log.d("JobService", "Job finished");
                jobFinished(parms, true);
            }
        })).start();
    }

    public boolean onStartJob(JobParameters parms) {
        this.doBackgroundWork(parms);
        return true;
    }

    public boolean onStopJob(JobParameters parms) {
        jobCancelled = true;
        return true;
    }
}
