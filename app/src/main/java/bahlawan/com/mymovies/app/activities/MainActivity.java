package bahlawan.com.mymovies.app.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import bahlawan.com.mymovies.R;
import bahlawan.com.mymovies.app.Connection;
import bahlawan.com.mymovies.app.fragments.FavouriteFragment;
import bahlawan.com.mymovies.app.fragments.NowPlayingFragment;
import bahlawan.com.mymovies.app.fragments.SortingFragmentDialog;
import bahlawan.com.mymovies.app.fragments.UpcomingFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean  isChecked;
    private static final String TAG = "MainActivity";
    public static int chosen_option;
    public static int fragment_index;
    MenuItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        createNotificationChannel();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_now_playing);
        showFragment(NowPlayingFragment.class);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        item = menu.findItem(R.id.action_settings);
        item.setChecked(getFromSharePrefernces("cec1"));  // get the notification button state
        if (item.isChecked()){
            scheduleJob();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if (item.isChecked()) {
                item.setChecked(false);
                Toast.makeText(this,"not checked",Toast.LENGTH_SHORT).show();
                saveInSharedPrefernces("cec1",false);
                cancelJob();
            } else{
                item.setChecked(true);
                scheduleJob();
                saveInSharedPrefernces("cec1",true);
                Toast.makeText(this,"checked",Toast.LENGTH_SHORT).show();
            }
        }else if (id == R.id.action_sort){
              SortingFragmentDialog dialog = new SortingFragmentDialog();
              Bundle extras = new Bundle();
              extras.putInt("current_index",fragment_index);
              extras.putInt("sorting_option",chosen_option);
              dialog.setArguments(extras);
              dialog.show(getSupportFragmentManager(),"OptionDialog");
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_now_playing) {
            showFragment(NowPlayingFragment.class);
        } else if (id == R.id.nav_upcoming) {
            showFragment(UpcomingFragment.class);

        } else if (id == R.id.nav_fav) {
            showFragment(FavouriteFragment.class);
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void showFragment(Class fragmentClass){
        android.support.v4.app.Fragment fragment = null;
        try {
            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (fragment.getClass().equals(NowPlayingFragment.class)){
            fragment_index = 1;
        }else if (fragment.getClass().equals(UpcomingFragment.class)){
            fragment_index = 2;
        }else if (fragment.getClass().equals(FavouriteFragment.class)){
            fragment_index = 3;
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent,fragment)
                .commit();
    }

    private boolean getFromSharePrefernces(String key){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME",MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    private void saveInSharedPrefernces(String key,boolean value){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, Connection.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)  // the maximum period to throw notification
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "Job cancelled");
    }
}

