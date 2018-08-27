package com.stuffbox.webscraper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    String searchurl;
    DataAdapter mDataAdapter;
    private  ArrayList<String> mEpisodeList=new ArrayList<>();
public static ArrayList<Bitmap> mImage=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Description().execute();
        String x=getIntent().getStringExtra("sentfromhere");
        if(x!=null)
        {
            finish();
            Intent intent = new Intent(this, this.getClass());
            startActivity(intent);

        }
        final EditText editText=findViewById(R.id.edittext);
        Button b=findViewById(R.id.ad);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
String y=editText.getText().toString();
            Log.i("checking",y);
StringBuffer s=new StringBuffer(y);
for(int i=0;i<s.length();i++)
{
    if(s.charAt(i)==' ')
    {
        s.setCharAt(i,'%');
    }
}
searchurl="https://www5.gogoanimes.tv//search.html?keyword="+s.toString();
        Log.i("CHECKING",searchurl);
Intent intent=new Intent(getApplicationContext(),AnimeFinder.class);
intent.putExtra("searchingstring",searchurl);
startActivity(intent);

            }
        });

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
                org.jsoup.nodes.Document mBlogDocument = Jsoup.connect(url).get();
                Log.i("soja",String.valueOf(mBlogDocument));
                Elements mElementDataSize=mBlogDocument.select("div[class=last_episodes loaddub]").select("ul[class=items]").select("li");
                int mElementSize = mElementDataSize.size();
                for (int i = 0; i < mElementSize; i++) {
                    Elements mElementAnimeName=mBlogDocument.select("p[class=name]").select("a").eq(i);
                    String mAnimenName= mElementAnimeName.text();
                    Log.i("zy",mAnimenName);
Elements mElementAnimeLink= mBlogDocument.select("p[class=name]").select("a").eq(i);
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
            RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.act_recyclerview);
            mDataAdapter = new DataAdapter(getApplicationContext(),MainActivity.this, mAnimeList, mSiteLink, mImageLink,mEpisodeList);
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