package com.stuffbox.webscraper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AnimeFinder extends AppCompatActivity {
    private ArrayList<String> mAnimeList = new ArrayList<>();
    private ArrayList<String> mSiteLink = new ArrayList<>();
    private ArrayList<String> mImageLink = new ArrayList<>();
    private  ArrayList<String> mEpisodeList=new ArrayList<>();
    animefinderadapter mDataAdapter;

    public static ArrayList<Bitmap> mImage=new ArrayList<>();
    String searchurl;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animefinder);
        searchurl=getIntent().getStringExtra("searchingstring");
     //   Log.i("chalrhahaikya",searchurl);

        new Searching().execute();
    }
    private class Searching extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(AnimeFinder.this);
            mProgressDialog.setTitle("Anime");
            mProgressDialog.setMessage("Searching...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                org.jsoup.nodes.Document searching = Jsoup.connect(searchurl).get();
             //   Log.i("asas",String.valueOf(searching));
                Elements elements=searching.select("div[class=main_body]").select("div[class=last_episodes]").select("ul[class=items]").select("li");
              //  Log.i("haiyanhi",String.valueOf(elements.size()));
                for(int i=0;i<elements.size();i++)
                {
                    String animelink=elements.select("div[class=img]").eq(i).select("a").attr("abs:href");
                    String animename=elements.select("div[class=img]").eq(i).select("a").attr("title");
                    String imagelink=elements.select("div[class=img]").eq(i).select("img").attr("src");
               //     Log.i("working2",animename);
                //    Log.i("working1",imagelink);
                    mAnimeList.add(animename);
                    mImageLink.add(imagelink);
                    mSiteLink.add(animelink);
                    mEpisodeList.add("");

               //     Log.i("working",animelink);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            RecyclerView  recyclerView=findViewById(R.id.recyclerview);
            mProgressDialog.dismiss();
            mDataAdapter = new animefinderadapter(getApplicationContext(),AnimeFinder.this, mAnimeList, mSiteLink, mImageLink,mEpisodeList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            recyclerView.setItemViewCacheSize(30);

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mDataAdapter);

        }
    }
}

