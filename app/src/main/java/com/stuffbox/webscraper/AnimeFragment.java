package com.stuffbox.webscraper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AnimeFragment extends Fragment {
  //  private  String url="https://www12.gogoanimes.tv/page-recent-release.html?page=1&type=2";
    private String url;
    private ArrayList<String> dubAnimeList=new ArrayList<>();
    private ArrayList<String> dubSiteLink = new ArrayList<>();
    private ArrayList<String> dubImageLink = new ArrayList<>();
    private  ArrayList<String> dubEpisodeList=new ArrayList<>();
    View view;
    DataAdapter mDataAdapter;

    RecyclerView mRecyclerView;
   public static AnimeFragment newInstance(String url)
   {
       Bundle bundle=new Bundle();
       bundle.putString("url",url);
       AnimeFragment dubFragment=new AnimeFragment();
        dubFragment.setArguments(bundle);
        return  dubFragment;
   }
   public  void readBundle(Bundle bundle)
   {
       if(bundle!=null)
       {
           url=bundle.getString("url");

       }
   }

    ProgressBar progressBar;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.dublayout,container,false);
        progressBar=view.findViewById(R.id.progress);
        readBundle(getArguments());
        new Dub().execute();
        return view;
    }

    private class Dub extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

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
                    dubAnimeList.add(mAnimenName);
                    dubSiteLink.add(mlink);
                    //       Log.i("imageee",imagelink);
                    dubImageLink.add(imagelink);
                    //  ImageView imageView;
                    dubEpisodeList.add(episodeno);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mRecyclerView    = (RecyclerView) view.findViewById(R.id.act_recyclerview);
            mDataAdapter = new DataAdapter(view.getContext(), dubAnimeList, dubSiteLink, dubImageLink, dubEpisodeList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
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
            progressBar.setVisibility(View.GONE);

        }
    }
}
