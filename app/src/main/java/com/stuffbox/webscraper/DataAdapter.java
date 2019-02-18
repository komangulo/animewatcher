package com.stuffbox.webscraper;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.net.Uri;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private ArrayList<String> mAnimeList = new ArrayList<>();
    private ArrayList<String> mSiteLink = new ArrayList<>();
    private ArrayList<String > mImageLink=new ArrayList<>();
    private  ArrayList<String> mEpisodeList=new ArrayList<>();
    int size;
private Context context;
    SQLiteDatabase recent;
   public DataAdapter(Context context, ArrayList<String> AnimeList, ArrayList<String> SiteList, ArrayList<String> ImageList, ArrayList<String> EpisodeList) {
        this.mAnimeList = AnimeList;
        this.mSiteLink = SiteList;
        this.context=context;
      //  this.mImage = ImageList;
       this.mImageLink=ImageList;
        this.mEpisodeList=EpisodeList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        private TextView title, episodeno;
        private Uri animeuri,imageuri;
        private ImageView imageofanime;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.animename);
            episodeno = (TextView) view.findViewById(R.id.episodeno);
           imageofanime=(ImageView) view.findViewById(R.id.img);
           cardView=(CardView) view.findViewById(R.id.cardview);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_data, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.title.setText(mAnimeList.get(position));
       holder.episodeno.setText(mEpisodeList.get(position));
        holder.animeuri= Uri.parse(mSiteLink.get(position));
         recent=context.openOrCreateDatabase("recent",Context.MODE_PRIVATE,null);
         holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,WatchVideo.class);
                intent.putExtra("link",mSiteLink.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                int ep=holder.episodeno.getText().toString().lastIndexOf(" ");
                size=0;
                recent.execSQL("delete from anime where EPISODELINK='"+holder.animeuri.toString()+"'");
           //     Log.i("deletingsql","delete from anime where EPISODELINK='"+holder.animeuri.toString()+"'");
                String z="'"+ holder.title.getText().toString()+"','"+holder.episodeno.getText().toString()+"','"+holder.animeuri.toString()+"','"+mImageLink.get(position)+"'";

                recent.execSQL("INSERT INTO anime VALUES("+z+");");
           //     Log.i("loggingsql",z);

                intent.putExtra("noofepisodes",holder.episodeno.getText().toString().substring(ep+1,holder.episodeno.getText().toString().length()));
                intent.putExtra("animename",holder.title.getText().toString());
                intent.putExtra("imagelink",mImageLink.get(position));
                intent.putExtra("size",size);
                context.getApplicationContext().startActivity(intent);
            }
        });
        Picasso.get().load(mImageLink.get(position)).into(holder.imageofanime);
      //  new Imageloader(mImageLink.get(position),holder.imageofanime).execute();
    }
    @Override
    public int getItemCount() {
        return mAnimeList.size();
    }

}

