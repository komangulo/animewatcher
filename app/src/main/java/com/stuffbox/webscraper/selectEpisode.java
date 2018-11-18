package com.stuffbox.webscraper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class selectEpisode extends AppCompatActivity {
    String link;
    String animename,animenameforrecents;
    private ArrayList<String> mEpisodeList=new ArrayList<>();
    ProgressDialog mProgressDialog;
    SQLiteDatabase recent;
    private ArrayList<String> mSiteLink = new ArrayList<>();
    episodeadapter mDataAdapter;
    String imagelink;
    EditText editText;
    TextView plotsummary;
    String summary;
    @Override
    protected   void onCreate(Bundle savedInstanceState)
    {
     //   Button button=findViewById(R.id.)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episodeselector);
         recent=openOrCreateDatabase("recent",MODE_PRIVATE,null);
        plotsummary=findViewById(R.id.plotsummary);
        animenameforrecents=getIntent().getStringExtra("animename");
        link=getIntent().getStringExtra("link");
        imagelink=getIntent().getStringExtra("imageurl");
        StringBuffer b=new StringBuffer();
        for(int i=0;i<link.length();i++)
        {
            if(link.charAt(i)=='y')
            {
                if(link.charAt(i+1)=='/')
                {
                    for(int j=i+2;j<link.length();j++)
                        b.append(String.valueOf(link.charAt(j)));
                    break;
                }
            }

        }

    //    Log.i("marjabe",b.toString());
animename=b.toString();
        new Searching().execute();
 editText=findViewById(R.id.episodeno);

Button button=findViewById(R.id.episodeselector);
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!editText.getText().toString().equals("")) {
                int episodeno = Integer.parseInt(String.valueOf(editText.getText()));
                Intent intent = new Intent(getApplicationContext(), WatchVideo.class);
                intent.putExtra("link", mSiteLink.get(episodeno - 1));

                intent.putExtra("noofepisodes", String.valueOf(mEpisodeList.size()));
                String z="'"+ animenameforrecents+"','Episode "+episodeno+"','"+mSiteLink.get(episodeno-1)+"','"+imagelink+"'";
         //       Log.i("loggingsql",z);
                recent.execSQL("delete from anime where EPISODELINK='"+mSiteLink.get(episodeno-1)+"'");
                recent.execSQL("INSERT INTO anime VALUES("+z+");");                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("animename",animenameforrecents);
                intent.putExtra("imagelink",imagelink);
                getApplicationContext().startActivity(intent);
            }
            else
            {   editText.requestFocus();
                editText.setError("Enter episode no first");
            }
        }
    });}

    private class Searching extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(selectEpisode.this);
            mProgressDialog.setTitle("Anime");
            mProgressDialog.setMessage("Loading Episodes...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                org.jsoup.nodes.Document searching = Jsoup.connect(link).get();
             //   Log.i("zyx",String.valueOf(searching));
                Elements elements=searching.select("div[class=anime_video_body]").select("ul[id=episode_page]").select("li");
           //     Log.i("checkinga",String.valueOf(elements.size()));
          //   for(int i=0;i<elements.size();i++)
           //      Log.i("ptanhikya",String.valueOf(elements.select("a").eq(i).html()));
                summary=searching.select("p[class=type]").eq(1).html();
                int index=summary.lastIndexOf(">");
                summary=summary.substring(index+1,summary.length());
             String a=String.valueOf(elements.select("a").eq(elements.size()-1).html());
             StringBuffer b=new StringBuffer();
          //      Log.i("ptanhikya",String.valueOf(a));
                for(int i=0;i<a.length();i++)
                {
                    if(a.charAt(i)=='-')
                    {
                        for(int j=i+1;j<a.length();j++)
                            b.append(a.charAt(j));
                    }
                }
                int x= Integer.parseInt(b.toString());
             //   Log.i("ptanhikya",String.valueOf(x));
for(int i=1;i<=x;i++)
{
    String c ="https://www8.gogoanimes.tv/"+animename+"-episode-"+i;
    mEpisodeList.add(String.valueOf(i));

    mSiteLink.add(c);
}
            }


            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.xyza);
            mDataAdapter = new episodeadapter(getApplicationContext(),selectEpisode.this, mSiteLink,mEpisodeList,imagelink,animenameforrecents);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setHasFixedSize(true);
            if(mEpisodeList.size()==1)
            {
                Intent intent=new Intent(getApplicationContext(),WatchVideo.class);
                intent.putExtra("link",mSiteLink.get(0));
                intent.putExtra("noofepisodes",String.valueOf(mEpisodeList.size()));
                String z="'"+ animenameforrecents+"','Episode "+1+"','"+mSiteLink.get(0)+"','"+imagelink+"'";
             //   Log.i("loggingsql",z);
                recent.execSQL("delete from anime where EPISODELINK='"+mSiteLink.get(0)+"'");

                recent.execSQL("INSERT INTO anime VALUES("+z+");");                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("animename",animenameforrecents);
                intent.putExtra("imagelink",imagelink);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
      //      for(int i=0;i<mEpisodeList.size();i++)
       //     {
            //    Log.i("checkingit",mEpisodeList.get(i));
           //     Log.i("checkingthat",mSiteLink.get(i));

       //     }

  //          mRecyclerView.setDrawingCacheEnabled(true);
     //       mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
     //       mRecyclerView.setItemViewCacheSize(20);

            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(mDataAdapter);
            mProgressDialog.dismiss();
            editText.setHint("Episode no between 1 to "+mEpisodeList.size());
            plotsummary.setText(plotsummary.getText()+" "+summary);
editText.setFilters(new InputFilter[]{
        new InputFilterMinMax(1,mEpisodeList.size())
});        }
    }

}
