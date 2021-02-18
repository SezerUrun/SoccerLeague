package com.example.soccerleague.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.soccerleague.MainActivity;
import com.example.soccerleague.MainActivity2;
import com.example.soccerleague.R;

import java.util.List;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private int count;

    private static List<String> TAB_TITLES;
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public static void setTabTitles(List<String> tabTitles) {
        TAB_TITLES = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public String getPageTitle(int position) {
        return TAB_TITLES.get(position);
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    public static List<String> getTabTitles() {
        return TAB_TITLES;
    }

    public Context getmContext() {
        return mContext;
    }


}