package com.stuffbox.webscraper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class Recent extends AppCompatActivity {
    private ArrayList<String> mAnimeList = new ArrayList<>();
    private ArrayList<String> mSiteLink = new ArrayList<>();
    private ArrayList<String> mImageLink = new ArrayList<>();
    String searchurl;
    Toolbar toolbar;
    DataAdapter mDataAdapter;
    private  ArrayList<String> mEpisodeList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animefinder);
        SQLiteDatabase recent=openOrCreateDatabase("recent",MODE_PRIVATE,null);
        Cursor resultSet = recent.rawQuery("Select * from anime",null);
        resultSet.moveToLast();
        //resultSet.

       // Log.i("soja",resultSet.getString(0));
               for(int i=resultSet.getCount()-1;i>=0;i--) {
                   mAnimeList.add(resultSet.getString(0));
                   mEpisodeList.add(resultSet.getString(1));
                   mSiteLink.add(resultSet.getString(2));
                   mImageLink.add(resultSet.getString(3));
                   resultSet.move(-1);
               }
        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mDataAdapter = new DataAdapter(this, mAnimeList, mSiteLink, mImageLink,mEpisodeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setItemViewCacheSize(20);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mDataAdapter);
      //  mProgressDialog.dismiss();


    }
}
