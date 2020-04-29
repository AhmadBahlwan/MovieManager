package bahlawan.com.mymovies.app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import bahlawan.com.mymovies.R;
import bahlawan.com.mymovies.app.fragments.FavouriteFragment;
import bahlawan.com.mymovies.app.fragments.NowPlayingFragment;
import bahlawan.com.mymovies.app.fragments.UpcomingFragment;

public class SortingFragmentDialog extends DialogFragment {
    int current_index;
    Fragment fragment;
    RadioButton option;

    private int getFromSharePrefernces(String key) {
        return getContext().getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE).getInt(key, R.id.most_popular);
    }

    // save the chosen option of sorting
    private void saveInSharedPrefernces(String key, int id) {
        Editor editor = getContext().getSharedPreferences("PROJECT_NAME", Context.MODE_PRIVATE).edit();
        editor.putInt(key, id);
        editor.commit();
    }





    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.sorting_options, container, false);
        final Dialog dialog = this.getDialog();
        if(getArguments() != null) {
            current_index = getArguments().getInt("current_index");
        }

        switch(current_index) {
            case 1:
                fragment = new NowPlayingFragment();
                option = view.findViewById(getFromSharePrefernces("NOW_PLAYING"));
                break;
            case 2:
                fragment = new UpcomingFragment();
                option = view.findViewById(this.getFromSharePrefernces("UPCOMING"));
                break;
            case 3:
                fragment = new FavouriteFragment();
                option = view.findViewById(this.getFromSharePrefernces("FAVOURITE"));
                break;
        }
        Log.d("option",String.valueOf(option.getId()));
        option.setChecked(true);
        dialog.setTitle("Sort By");
        final Editor editor = getContext().getSharedPreferences("option id", Context.MODE_PRIVATE).edit();
        final RadioGroup group = view.findViewById(R.id.sorting_group);
                   group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                switch(current_index) {
                    case 1:
                        saveInSharedPrefernces("NOW_PLAYING", checkedId);
                        editor.putInt("now_playing_fragment", checkedId);
                        editor.apply();
                        break;
                    case 2:
                        saveInSharedPrefernces("UPCOMING", checkedId);
                        editor.putInt("upcoming_fragment", checkedId);
                        editor.apply();
                        break;
                    case 3:
                        saveInSharedPrefernces("FAVOURITE", checkedId);
                        editor.putInt("favourite_fragment", checkedId);
                        editor.apply();
                        break;
                }

                option.setChecked(false);
                option = group.findViewById(checkedId);
                Log.d("option2",String.valueOf(checkedId));
                option.setChecked(true);
                dialog.dismiss();
                showFragment(fragment.getClass());
            }
        });
        return view;
    }

    public void showFragment(Class fragmentClass){
        android.support.v4.app.Fragment fragment = null;
        try {
            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.flContent,fragment)
                .commit();
    }
}
