package com.stuffbox.webscraper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    //rivate static final Object Context = ;
    private ProgressDialog mProgressDialog;
    // private String url = "https://www.yudiz.com/blog/";
    private  String url= "https://www8.gogoanimes.tv/";
    private ArrayList<String> mAnimeList = new ArrayList<>();
    private ArrayList<String> mSiteLink = new ArrayList<>();
    private ArrayList<String> mImageLink = new ArrayList<>();
    String searchurl;
    int flag=1;
    RecyclerView mRecyclerView;
    ProgressBar progressBar;
    private ArrayList<String> mAnimeList1 = new ArrayList<>();
    private ArrayList<String> mSiteLink1 = new ArrayList<>();
    private ArrayList<String> mImageLink1 = new ArrayList<>();
    private  ArrayList<String> mEpisodeList1=new ArrayList<>();
    Searching x=new Searching();
    LinearLayout noanime;
    Toolbar toolbar;
    DataAdapter mDataAdapter;
    animefinderadapter   DataAdapter;
    private  ArrayList<String> mEpisodeList=new ArrayList<>();
    public static ArrayList<Bitmap> mImage=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MediaPlayer mediaPlayer= MediaPlayer.create(this,R.raw.tuturu);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
        if (!haveNetworkConnection(this)) {
          //  LinearLayout linearLayout=findViewById(R.id.visib);
       //     linearLayout.setVisibility(View.GONE);
            LinearLayout linearLayout1=findViewById(R.id.notvisiblelinearlayout);
            linearLayout1.setVisibility(View.VISIBLE);
            RecyclerView recyclerView=findViewById(R.id.act_recyclerview);
            recyclerView.setVisibility(View.GONE);

        }
        else
        {

            new Description().execute();

            String x = getIntent().getStringExtra("sentfromhere");
            SQLiteDatabase recent = openOrCreateDatabase("recent", MODE_PRIVATE, null);
            recent.execSQL("CREATE TABLE IF NOT EXISTS anime(Animename VARCHAR,Episodeno VARCHAR,EPISODELINK VARCHAR,IMAGELINK VARCHAR)");
             progressBar=findViewById(R.id.progress);
            if (x != null) {
                finish();
                Intent intent = new Intent(this, this.getClass());
                startActivity(intent);

            }
            toolbar = findViewById(R.id.tool);
            setSupportActionBar(toolbar);
            noanime=findViewById(R.id.noanime);

        }
    }
    public static boolean haveNetworkConnection(android.content.Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.drawer, menu);
        MenuItem search=menu.findItem(R.id.action_search);
        MenuItem animelist=menu.findItem(R.id.animelist);
        animelist.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
            Intent i=new Intent(getApplicationContext(),AnimeList.class);
            startActivity(i);

                return false;
            }
        });
        MenuItem menuItem=menu.findItem(R.id.recent);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent=new Intent(getApplicationContext(),Recent.class);
                startActivity(intent);
                return false;
            }
        });
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText=newText.toLowerCase();
        noanime.setVisibility(View.GONE);

        if(newText.length()>=3)
        {
            mRecyclerView.setVisibility(View.GONE);
            searchurl = "https://www8.gogoanimes.tv//search.html?keyword=" + newText.toString();
            if(x.getStatus()==AsyncTask.Status.RUNNING)
                x.cancel(true);
            x=new Searching();
            x.execute();

        }

        if(newText.length()<=2)
        {
            if(x.getStatus()==AsyncTask.Status.RUNNING)
                x.cancel(true);
            RecyclerView  recyclerView=findViewById(R.id.recyclerview);
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

        }
        return false;
    }
    private class Searching extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RecyclerView  recyclerView=findViewById(R.id.recyclerview);

            recyclerView.setVisibility(View.GONE);

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                org.jsoup.nodes.Document searching = Jsoup.connect(searchurl).get();
                //   Log.i("asas",String.valueOf(searching));
                DataAdapter=new animefinderadapter();
                DataAdapter.notifyItemRangeRemoved(0,mAnimeList.size());


                mAnimeList.clear();
                mSiteLink.clear();
                mImageLink.clear();
                mEpisodeList.clear();
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
            progressBar.setVisibility(View.GONE);

            //   mProgressDialog.dismiss();
            if(mAnimeList.size()==0)
                noanime.setVisibility(View.VISIBLE) ;
            else {
                recyclerView.setVisibility(View.VISIBLE);

                DataAdapter = new animefinderadapter(getApplicationContext(), mAnimeList, mSiteLink, mImageLink, mEpisodeList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setHasFixedSize(true);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                recyclerView.setItemViewCacheSize(30);

                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(DataAdapter);
            }
        }
    }
    private class Description extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog( MainActivity.this);
            mProgressDialog.setTitle("Anime");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();

        }

        @Override
        protected Void  doInBackground(Void... params) {
            try {
                //    Log.d("chalja",url);
                org.jsoup.nodes.Document mBlogDocument = Jsoup.connect(url).get();
                //      Log.i("soja",String.valueOf(mBlogDocument));
                Elements mElementDataSize=mBlogDocument.select("div[class=last_episodes loaddub]").select("ul[class=items]").select("li");
                int mElementSize = mElementDataSize.size();
                for (int i = 0; i < mElementSize; i++) {
                    Elements mElementAnimeName=mBlogDocument.select("p[class=name]").select("a").eq(i);
                    String mAnimenName= mElementAnimeName.text();
                    if(mAnimenName.contains("[email protected]"))
                       mAnimenName= mAnimenName.replace("[email protected]","IDOLM@STER");
                    //      Log.i("zy",mAnimenName);
                    Elements mElementAnimeLink= mBlogDocument.select("p[class=name]").select("a").eq(i);
                    String mlink=mElementAnimeLink.attr("abs:href");
                    Elements mElementImageLink=mBlogDocument.select("div[class=img]").select("img").eq(2*i);
                    String imagelink=mElementImageLink.attr("src");
                    Elements mELementEpisodeno =mBlogDocument.select("p[class=episode]").eq(i);
                    //       Log.i("check",mELementEpisodeno.text());
                    String episodeno=mELementEpisodeno.text();
                    mAnimeList1.add(mAnimenName);
                    mSiteLink1.add(mlink);
                    //       Log.i("imageee",imagelink);
                    mImageLink1.add(imagelink);
                    //  ImageView imageView;
                    mEpisodeList1.add(episodeno);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mRecyclerView    = (RecyclerView) findViewById(R.id.act_recyclerview);
            mDataAdapter = new DataAdapter(getApplicationContext(), mAnimeList1, mSiteLink1, mImageLink1, mEpisodeList1);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setDrawingCacheEnabled(true);
            mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            mRecyclerView.setItemViewCacheSize(20);
            //  mAnimeList1 = new ArrayList<>(mAnimeList);
            //  mEpisodeList1 = new ArrayList<>(mEpisodeList);
            //  mSiteLink1 = new ArrayList<>(mSiteLink);
            //  mImageLink1 = new ArrayList<>(mImageLink);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mDataAdapter);
            mProgressDialog.dismiss();

        }
    }

}