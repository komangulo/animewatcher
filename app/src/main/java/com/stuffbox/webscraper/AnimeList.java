package com.stuffbox.webscraper;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AnimeList extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ArrayList<String> animename=new ArrayList<>();
    private  ArrayList<String> animelink=new ArrayList<>();
    AnimeFindAdapter mDataAdapter;
    Toolbar toolbar;

    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animelistrecyclerview);
        toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        try
        {
            InputStream is = getResources().openRawResource(R.raw.animelist);
            String s = IOUtils.toString(is);
            IOUtils.closeQuietly(is);
            //   org.json.simple.JSONArray array=(org.json.simple.JSONArray) obj;
          //  array.forEach();
            JSONArray jsonArray=new JSONArray(s);
      for(int i=0;i<jsonArray.length();i++)
      {
          JSONObject a=jsonArray.getJSONObject(i);
          JSONObject anime=a.getJSONObject("anime");
          animelink.add((String)anime.get("link"));
          animename.add((String) anime.get("Anime name"));

      }
                   } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        RecyclerView recyclerView=findViewById(R.id.animelistrecyclerview);
           mDataAdapter = new AnimeFindAdapter(getApplicationContext(), animename, animelink);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //    recyclerView.setHasFixedSize(true);
    //    recyclerView.setDrawingCacheEnabled(true);
    //    recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    //    recyclerView.setItemViewCacheSize(30);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mDataAdapter);
    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.animelist, menu);
        MenuItem search=menu.findItem(R.id.anime_list_search);
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
        ArrayList<String> newlinklist=new ArrayList<>();
        ArrayList<String> newanimelist=new ArrayList<>();
        for(int i=0;i<animename.size();i++)
        {
            if(animename.get(i).toLowerCase().contains(newText))
            {
                newanimelist.add(animename.get(i));
                newlinklist.add(animelink.get(i));
            }
        }
        mDataAdapter.setFilter(newanimelist,newlinklist);


        return false;
    }
}

