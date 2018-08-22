package com.stuffbox.webscraper;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;


public class MainActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
   // private String url = "https://www.yudiz.com/blog/";
    private  String url= "https://www5.gogoanimes.tv/";
    private ArrayList<String> mAnimeList = new ArrayList<>();
    private ArrayList<String> mSiteLink = new ArrayList<>();
    private ArrayList<String> mImageLink = new ArrayList<>();
    private  ArrayList<String> mEpisodeList=new ArrayList<>();
public static ArrayList<Bitmap> mImage=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Description().execute();

    }

    private class Description extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Anime");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                org.jsoup.nodes.Document mBlogDocument = Jsoup.connect(url).get();
                Log.i("soja",String.valueOf(mBlogDocument));
                // Using Elements to get the Meta data
          //      Elements mElementDataSize = mBlogDocument.select("div[class=author-date]");
                Elements mElementDataSize=mBlogDocument.select("div[class=last_episodes loaddub]").select("ul[class=items]").select("li");
                // Locate the content attribute
                int mElementSize = mElementDataSize.size();

                for (int i = 0; i < mElementSize; i++) {
             //       Elements mElementAuthorName = mBlogDocument.select("span[class=vcard author post-author test]").select("a").eq(i);
                //    String mAuthorName = mElementAuthorName.text();
                    Elements mElementAnimeName=mBlogDocument.select("p[class=name]").select("a").eq(i);
                    String mAnimenName= mElementAnimeName.text();
                    Log.i("zy",mAnimenName);
             //       Elements mElementBlogUploadDate = mBlogDocument.select("span[class=post-date updated]").eq(i);
                 //   String mBlogUploadDate = mElementBlogUploadDate.text();
Elements mElementAnimeLink= mBlogDocument.select("p[class=name]").select("a");
            //      Elements mElementBlogTitle = mBlogDocument.select("h2[class=entry-title]").select("a").eq(i);
                 //   String mBlogTitle = mElementBlogTitle.text();
String mlink=mElementAnimeLink.attr("abs:href");
Elements mElementImageLink=mBlogDocument.select("div[class=img]").select("img").eq(2*i);
                    String imagelink=mElementImageLink.attr("src");
                    Elements mELementEpisodeno =mBlogDocument.select("p[class=episode]").eq(i);
                    Log.i("check",mELementEpisodeno.text());
                    String episodeno=mELementEpisodeno.text();
                    mAnimeList.add(mAnimenName);
                    mSiteLink.add(mlink);
                    Log.i("imageee",imagelink);
                    mImageLink.add(imagelink);
                  //  ImageView imageView;
                    mEpisodeList.add(episodeno);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView

            RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.act_recyclerview);
//for(int i=0;i<mImageLink.size();i++)
   // new Imageloader(mImageLink.get(i)).execute();
//Log.i("size",String.valueOf(Imageloader.image.size()));
//mImage=Imageloader.image;
         //   DataAdapter mDataAdapter = new DataAdapter(MainActivity.this, mAnimeList, mSiteLink, mImage,mEpisodeList);
            DataAdapter mDataAdapter = new DataAdapter(MainActivity.this, mAnimeList, mSiteLink, mImageLink,mEpisodeList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setDrawingCacheEnabled(true);
            mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            mRecyclerView.setItemViewCacheSize(20);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mDataAdapter);
           mProgressDialog.dismiss();
        }
    }
}