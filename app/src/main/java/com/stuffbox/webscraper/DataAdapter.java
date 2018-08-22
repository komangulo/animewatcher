package com.stuffbox.webscraper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.HttpUrl;

/**
 * Created by jasbe on 22-08-2018.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private ArrayList<String> mAnimeList = new ArrayList<>();
    private ArrayList<String> mSiteLink = new ArrayList<>();
  //  private ArrayList<Bitmap> mImage = new ArrayList<>();
    private ArrayList<String > mImageLink=new ArrayList<>();
    private  ArrayList<String> mEpisodeList=new ArrayList<>();
    private Activity mActivity;
    private int lastPosition = -1;

   // public DataAdapter(MainActivity activity, ArrayList<String> AnimeList, ArrayList<String> SiteList, ArrayList<Bitmap> ImageList,ArrayList<String> EpisodeList) {
   public DataAdapter(MainActivity activity, ArrayList<String> AnimeList, ArrayList<String> SiteList, ArrayList<String> ImageList,ArrayList<String> EpisodeList) {
        this.mActivity = activity;
        this.mAnimeList = AnimeList;
        this.mSiteLink = SiteList;
      //  this.mImage = ImageList;
       this.mImageLink=ImageList;
        this.mEpisodeList=EpisodeList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title, episodeno;
        private Uri animeuri,imageuri;
        private ImageView imageofanime;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.animename);
            episodeno = (TextView) view.findViewById(R.id.episodeno);
           imageofanime=(ImageView) view.findViewById(R.id.img);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_data, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.title.setText(mAnimeList.get(position));
       holder.episodeno.setText(mEpisodeList.get(position));
        holder.animeuri= Uri.parse(mSiteLink.get(position));
       // holder.imageofanime.setImageBitmap(mImage.get(position));
        new Imageloader(mImageLink.get(position),holder.imageofanime).execute();
        //    holder.imageofanime.setImageBitmap(getBitmapFromURL(mImageLink.get(position)));
        // holder.tv_blog_upload_date.setText(mBlogUploadDateList.get(position));
    }

    @Override
    public int getItemCount() {
        return mAnimeList.size();
    }

}

