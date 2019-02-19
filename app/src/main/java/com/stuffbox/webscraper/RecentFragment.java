package com.stuffbox.webscraper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecentFragment extends Fragment {
    private ArrayList<String> mAnimeList = new ArrayList<>();
    private ArrayList<String> mSiteLink = new ArrayList<>();
    private ArrayList<String> mImageLink = new ArrayList<>();
    String searchurl;
    Toolbar toolbar;
    DataAdapter mDataAdapter;
    RecyclerView mRecyclerView;
    Cursor resultSet;
    private  ArrayList<String> mEpisodeList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.animefinder,container,false);
        SQLiteDatabase recent=getContext().openOrCreateDatabase("recent",Context.MODE_PRIVATE,null);
        resultSet = recent.rawQuery("Select * from anime",null);
        resultSet.moveToLast();
         mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        for(int i=resultSet.getCount()-1,count=0;i>=0;i--) {

            mAnimeList.add(resultSet.getString(0));
            mEpisodeList.add(resultSet.getString(1));
            mSiteLink.add(resultSet.getString(2));
            mImageLink.add(resultSet.getString(3));
            resultSet.move(-1);
        }
        mDataAdapter = new DataAdapter(getContext(), mAnimeList, mSiteLink, mImageLink,mEpisodeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setItemViewCacheSize(20);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mDataAdapter);
        return view;
    }

    @Override
    public void onPause() {
        mAnimeList.clear();
        mEpisodeList.clear();
        mSiteLink.clear();
        mImageLink.clear();
        super.onPause();
    }

    @Override
    public void onResume() {
        SQLiteDatabase recent=getContext().openOrCreateDatabase("recent",Context.MODE_PRIVATE,null);

        resultSet = recent.rawQuery("Select * from anime",null);
        resultSet.moveToLast();
        for(int i=resultSet.getCount()-1,count=0;i>=0;i--) {

            mAnimeList.add(resultSet.getString(0));
            mEpisodeList.add(resultSet.getString(1));
            mSiteLink.add(resultSet.getString(2));
            mImageLink.add(resultSet.getString(3));
            resultSet.move(-1);
        }
        mDataAdapter = new DataAdapter(getContext(), mAnimeList, mSiteLink, mImageLink,mEpisodeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setItemViewCacheSize(20);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mDataAdapter);
        super.onResume();
    }
}
