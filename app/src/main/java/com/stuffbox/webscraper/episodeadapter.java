package com.stuffbox.webscraper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class episodeadapter extends RecyclerView.Adapter<episodeadapter.MyViewHolder> {

    private ArrayList<String> mAnimeList = new ArrayList<>();
    private ArrayList<String> mSiteLink = new ArrayList<>();
    //  private ArrayList<Bitmap> mImage = new ArrayList<>();
    private ArrayList<String > mImageLink=new ArrayList<>();
    private  ArrayList<String> mEpisodeList=new ArrayList<>();
    private Activity mActivity;
    private int lastPosition = -1;
    private Context context;
    // public DataAdapter(MainActivity activity, ArrayList<String> AnimeList, ArrayList<String> SiteList, ArrayList<Bitmap> ImageList,ArrayList<String> EpisodeList) {
    public episodeadapter(Context context,selectEpisode activity, ArrayList<String> SiteList, ArrayList<String> EpisodeList) {
        this.mActivity = activity;
        this.mSiteLink = SiteList;
        this.context=context;
        //  this.mImage = ImageList;
        this.mEpisodeList=EpisodeList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
     //   private CardView cardView;

        private TextView  episodeno;
        private Uri animeuri,imageuri;
     //   private ImageView imageofanime;
        private Button button;

        public MyViewHolder(View view) {
            super(view);
           // title = (TextView) view.findViewById(R.id.animename);
            episodeno = (TextView) view.findViewById(R.id.textforepisode);
            button=view.findViewById(R.id.notbutton);
           // imageofanime=(ImageView) view.findViewById(R.id.img);
       //     cardView=(CardView) view.findViewById(R.id.cardview);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapterforepisode, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
   //     holder.title.setText(mAnimeList.get(position));
        holder.episodeno.setText(mEpisodeList.get(position));
        holder.animeuri= Uri.parse(mSiteLink.get(position));


      holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,WatchVideo.class);
                intent.putExtra("link",mSiteLink.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });
        // holder.imageofanime.setImageBitmap(mImage.get(position));
     //   new Imageloader(mImageLink.get(position),holder.imageofanime).execute();
        //    holder.imageofanime.setImageBitmap(getBitmapFromURL(mImageLink.get(position)));
        // holder.tv_blog_upload_date.setText(mBlogUploadDateList.get(position));
    }

    @Override
    public int getItemCount() {
        return mEpisodeList.size();
    }

}

