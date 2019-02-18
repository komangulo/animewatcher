package com.stuffbox.webscraper;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public  ViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
    Fragment fragment=null;
    if(position==1)
    {
        fragment=AnimeFragment.newInstance("https://www12.gogoanimes.tv/page-recent-release.html?page=1&type=2");


    }
    else
        fragment=AnimeFragment.newInstance("https://www12.gogoanimes.tv/");
    return  fragment;
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "SUB";
        }
        else if (position == 1)
        {
            title = "DUB";
        }

        return title;
    }
}
