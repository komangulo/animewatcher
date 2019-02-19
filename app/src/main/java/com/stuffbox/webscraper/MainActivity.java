package com.stuffbox.webscraper;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout noanime;
    String searchurl;
    AppBarLayout appBarLayout;
    private ArrayList<String> mAnimeList = new ArrayList<>();
    private ArrayList<String> mSiteLink = new ArrayList<>();
    private ArrayList<String> mImageLink = new ArrayList<>();
    private ArrayList<String> mEpisodeList = new ArrayList<>();
    RecyclerView mRecyclerView;
    DataAdapter mDataAdapter;
    FrameLayout frameLayout;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;

    animefinderadapter DataAdapter;
    Searching x = new Searching();
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frametest);
        final MediaPlayer mediaPlayer= MediaPlayer.create(this,R.raw.tuturu);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
            }
        });
        if(!haveNetworkConnection(getApplicationContext()))
        {
            LinearLayout linearLayout1=findViewById(R.id.notvisiblelinearlayout);
            linearLayout1.setVisibility(View.VISIBLE);
            appBarLayout=findViewById(R.id.tabtoolbar);
            appBarLayout.setVisibility(View.GONE);
            //  RecyclerView recyclerView=findViewById(R.id.act_recyclerview);
           // recyclerView.setVisibility(View.GONE);
        }else{


        toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        FragmentManager fm = getSupportFragmentManager();
        noanime=findViewById(R.id.noanime);
        appBarLayout=findViewById(R.id.tabtoolbar);
        ViewPagerAdapter viewPagerAdapter;
       // frameLayout=findViewById(R.id.frameLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        appBarLayout.setVisibility(View.VISIBLE);
            SQLiteDatabase recent = openOrCreateDatabase("recent", MODE_PRIVATE, null);
            recent.execSQL("CREATE TABLE IF NOT EXISTS anime(Animename VARCHAR,Episodeno VARCHAR,EPISODELINK VARCHAR,IMAGELINK VARCHAR)");
        progressBar=findViewById(R.id.progress2);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(1);

            //  FragmentTransaction fragmentTransaction = fm.beginTransaction();
      //  fragmentTransaction.replace(R.id.frameLayout, new DubFragment());
      //  fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.drawer, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        MenuItem animelist = menu.findItem(R.id.animelist);
        animelist.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(getApplicationContext(), AnimeList.class);
                startActivity(i);

                return false;
            }
        });
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                noanime.setVisibility(View.GONE);

                if (newText.length() >= 3) {
                   // mRecyclerView.setVisibility(View.GONE);
                    RecyclerView recyclerView = findViewById(R.id.recyclerview2);
                    recyclerView.setVisibility(View.VISIBLE);
                    appBarLayout.setVisibility(View.GONE);
                    searchurl = "https://www12.gogoanimes.tv//search.html?keyword=" + newText.toString();
                    if (x.getStatus() == AsyncTask.Status.RUNNING)
                        x.cancel(true);
                    x = new Searching();
                    x.execute();

                }

                if (newText.length() <= 2) {
                    if (x.getStatus() == AsyncTask.Status.RUNNING)
                        x.cancel(true);
                    RecyclerView recyclerView = findViewById(R.id.recyclerview2);
                    recyclerView.setVisibility(View.GONE);
                    progressBar=findViewById(R.id.progress2);
                    appBarLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                   // mRecyclerView.setVisibility(View.VISIBLE);

                }
                return false;
            }
        });
        return true;
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
    private class Searching extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RecyclerView recyclerView = findViewById(R.id.recyclerview2);

            recyclerView.setVisibility(View.GONE);

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                org.jsoup.nodes.Document searching = Jsoup.connect(searchurl).get();
                //   Log.i("asas",String.valueOf(searching));
                DataAdapter = new animefinderadapter();
                DataAdapter.notifyItemRangeRemoved(0, mAnimeList.size());


                mAnimeList.clear();
                mSiteLink.clear();
                mImageLink.clear();
                mEpisodeList.clear();
                Elements elements = searching.select("div[class=main_body]").select("div[class=last_episodes]").select("ul[class=items]").select("li");
                //  Log.i("haiyanhi",String.valueOf(elements.size()));
                for (int i = 0; i < elements.size(); i++) {
                    String animelink = elements.select("div[class=img]").eq(i).select("a").attr("abs:href");
                    String animename = elements.select("div[class=img]").eq(i).select("a").attr("title");
                    String imagelink = elements.select("div[class=img]").eq(i).select("img").attr("src");
                    //     Log.i("working2",animename);
                    //    Log.i("working1",imagelink);
                    mAnimeList.add(animename);
                    mImageLink.add(imagelink);
                    mSiteLink.add(animelink);
                    mEpisodeList.add("");

                    //     Log.i("working",animelink);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            RecyclerView recyclerView = findViewById(R.id.recyclerview2);
            progressBar.setVisibility(View.GONE);

            //   mProgressDialog.dismiss();
            if (mAnimeList.size() == 0)
                noanime.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        if(!(viewPager.getCurrentItem()==1))
            viewPager.setCurrentItem(1);
        else
        super.onBackPressed();
    }
}
