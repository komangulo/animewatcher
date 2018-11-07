package com.stuffbox.webscraper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


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
    int size;
private Context context;
    SQLiteDatabase recent;
   // public DataAdapter(MainActivity activity, ArrayList<String> AnimeList, ArrayList<String> SiteList, ArrayList<Bitmap> ImageList,ArrayList<String> EpisodeList) {
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

      /*  holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // WatchVideo.link=mSiteLink.get(position);

                Intent intent=new Intent(context,WatchVideo.class);
                intent.putExtra("link",mSiteLink.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                int ep=holder.episodeno.getText().toString().lastIndexOf(" ");
           //     ep=Integer.parseInt(h)
                 //   recent.execSQL("INSERT INTO anime VALUES('Beyblade Burst Chouzetsu','Episode 18','https://www04.gogoanimes.tv/beyblade-burst-chouzetsu-episode-18','https://images.gogoanime.tv/cover/beyblade-burst-chouzetsu.png');");
                String z="'"+ mAnimeList.get(position)+"','"+mEpisodeList.get(position)+"','"+mSiteLink.get(position)+"','"+mImageLink.get(position)+"'";
                recent.execSQL("INSERT INTO anime VALUES("+z+");");
                intent.putExtra("noofepisodes",holder.episodeno.getText().toString().substring(ep+1,holder.episodeno.getText().toString().length()));
                context.startActivity(intent);
            }
        }); */
         holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,WatchVideo.class);
                intent.putExtra("link",mSiteLink.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                int ep=holder.episodeno.getText().toString().lastIndexOf(" ");
            //   ep=Integer.parseInt(h)
              //  int size=Episodesize(holder.title.getText().toString());


                size=0;
                recent.execSQL("delete from anime where EPISODELINK='"+holder.animeuri.toString()+"'");
                Log.i("deletingsql","delete from anime where EPISODELINK='"+holder.animeuri.toString()+"'");
                String z="'"+ holder.title.getText().toString()+"','"+holder.episodeno.getText().toString()+"','"+holder.animeuri.toString()+"','"+mImageLink.get(position)+"'";

                recent.execSQL("INSERT INTO anime VALUES("+z+");");
                Log.i("loggingsql",z);

                intent.putExtra("noofepisodes",holder.episodeno.getText().toString().substring(ep+1,holder.episodeno.getText().toString().length()));
                intent.putExtra("animename",holder.title.getText().toString());
                intent.putExtra("imagelink",mImageLink.get(position));
                intent.putExtra("size",size);
                context.getApplicationContext().startActivity(intent);
            }
        });
/*holder.imageofanime.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(context,WatchVideo.class);
        intent.putExtra("link",mSiteLink.get(position));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int ep=holder.episodeno.getText().toString().lastIndexOf(" ");
      //  ep=Integer.parseInt(h)
        String z="'"+ mAnimeList.get(position)+"','"+mEpisodeList.get(position)+"','"+mSiteLink.get(position)+"','"+mImageLink.get(position)+"'";
        recent.execSQL("INSERT INTO anime VALUES("+z+");");
        intent.putExtra("noofepisodes",holder.episodeno.getText().toString().substring(ep+1,holder.episodeno.getText().toString().length()));
        context.getApplicationContext().startActivity(intent);
    }
}); */
       // holder.imageofanime.setImageBitmap(mImage.get(position));
        new Imageloader(mImageLink.get(position),holder.imageofanime).execute();
        //    holder.imageofanime.setImageBitmap(getBitmapFromURL(mImageLink.get(position)));
        // holder.tv_blog_upload_date.setText(mBlogUploadDateList.get(position));
    }
public static int Episodesize(final String name)
{

    final int[] count = new int[1];
Runnable r=new Runnable() {
    int i;
    CountDownLatch l=new CountDownLatch(1);
    @Override
    public void run() {
        try {

Log.i("nameis",name);
Document searching=Jsoup.connect("https://www04.gogoanimes.tv//search.html?keyword="+name).get();

            Elements elements=searching.select("div[class=main_body]").select("div[class=last_episodes]").select("ul[class=items]").select("li");
            Log.i("haiyanhi",String.valueOf(elements.size()));
            for(i=0;i<elements.size();i++)
            {

                String animename=elements.select("div[class=img]").eq(i).select("a").attr("title");
                if(animename.equals(name))
                    break;


            }
            String animelink=elements.select("div[class=img]").eq(i).select("a").attr("abs:href");
            Document f=Jsoup.connect(animelink).get();
            Elements k=f.select("div[class=anime_video_body]").select("ul[id=episode_page]").select("li");
            Log.i("checkinga",String.valueOf(k.size()));
            for(int i=0;i<k.size();i++)
                Log.i("ptanhikya",String.valueOf(k.select("a").eq(i).html()));
            String a=String.valueOf(k.select("a").eq(k.size()-1).html());
            StringBuffer b=new StringBuffer();
            Log.i("ptanhikya",String.valueOf(a));
            for(int i=0;i<a.length();i++)
            {
                if(a.charAt(i)=='-')
                {
                    for(int j=i+1;j<a.length();j++)
                        b.append(a.charAt(j));
                }
            }
             count[0] = Integer.parseInt(b.toString());
            Log.i("loggingcount",String.valueOf(count[0]));
            l.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            l.await();
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

};

 return count[0];
}
    @Override
    public int getItemCount() {
        return mAnimeList.size();
    }

}

