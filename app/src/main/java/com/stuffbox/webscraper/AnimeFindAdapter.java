package com.stuffbox.webscraper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AnimeFindAdapter extends RecyclerView.Adapter<AnimeFindAdapter.MyViewHolder> {

    private ArrayList<String> mAnimeList = new ArrayList<>();
    private ArrayList<String> mSiteLink = new ArrayList<>();
    private int lastPosition = -1;
    private Context context;
    // public DataAdapter(MainActivity activity, ArrayList<String> AnimeList, ArrayList<String> SiteList, ArrayList<Bitmap> ImageList,ArrayList<String> EpisodeList) {
    public AnimeFindAdapter(Context context, ArrayList<String> AnimeList, ArrayList<String> SiteList) {
        this.mAnimeList = AnimeList;
        this.mSiteLink = SiteList;
        this.context=context;

    }
    public  AnimeFindAdapter()
    {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layout;

        private TextView title, episodeno;
       private Uri animeuri,imageuri;
     //   private ImageView imageofanime;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.animen);
          //  episodeno = (TextView) view.findViewById(R.id.episodeno);
          //  imageofanime=(ImageView) view.findViewById(R.id.img);
        //    cardView=(CardView) view.findViewById(R.id.cardview);
            layout=view.findViewById(R.id.layout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapterforanimelist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.title.setText(mAnimeList.get(position));
        holder.animeuri= Uri.parse(mSiteLink.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,selectEpisode.class);
                intent.putExtra("link",mSiteLink.get(position));
                intent.putExtra("animename",mAnimeList.get(position));
          //      intent.putExtra("imageurl",mImageLink.get(position));
                intent.putExtra("imageurl","https://images.gogoanime.tv/cover/yuuyuuhakusho-specials.png");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });

        // holder.imageofanime.se
        // tImageBitmap(mImage.get(position));
     //   new Imageloader(mImageLink.get(position),holder.imageofanime).execute();
        //    holder.imageofanime.setImageBitmap(getBitmapFromURL(mImageLink.get(position)));
        // holder.tv_blog_upload_date.setText(mBlogUploadDateList.get(position));
    }

    @Override
    public int getItemCount() {
        return mAnimeList.size();
    }
    public void setFilter(ArrayList<String>  animelist,ArrayList<String> animelink )
    {
        mAnimeList=new ArrayList<>();
        mAnimeList.addAll(animelist);
        mSiteLink=new ArrayList<>();
        mSiteLink.addAll(animelink);
        notifyDataSetChanged();
    }

}

