package com.stuffbox.webscraper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by jasbe on 22-08-2018.
 */

public class WatchVideo extends AppCompatActivity {
    String finallink;
    ProgressDialog mProgressDialog;
ImageButton nextepisode,prevepisode;
    String l;
    long time;
    String nextlink;
    int k=0;
    String link;
    int s;
    SQLiteDatabase recent;
    AlertDialog dialog;
    ProgressBar progressBar;
    int qualitysetter=0;
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private PlayerView playerView;
    private ArrayList<String> storinggoogleurls=new ArrayList<>();
    private boolean mExoPlayerFullscreen = false;
    int episodeno;
    int current;
    SimpleExoPlayer simpleExoPlayer;
    org.jsoup.nodes.Document mBlogDocument ;
    private int mResumeWindow;
    TextView title;
    String nextvideolink=null,previousvideolink=null;
    int epno;
    ImageButton qualityup,qualitydown;
    String animename,imagelink;
    private  ArrayList<String> storingquality=new ArrayList<>();
    com.google.android.exoplayer2.upstream.DataSource.Factory datasourcefactory;
    private long mResumePosition;
    ImageButton qualitychanger;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       setContentView(R.layout.videoviewer);
        recent=openOrCreateDatabase("recent",MODE_PRIVATE,null);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        title=findViewById(R.id.titleofanime);
        title.setVisibility(View.GONE);
        progressBar=findViewById(R.id.buffer);
        qualitychanger=findViewById(R.id.qualitychanger);
        if(finallink!=null)
        Log.i("voiz",finallink);
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);


        }
qualitydown=findViewById(R.id.qualitydown);
        nextepisode=findViewById(R.id.exo_nextvideo);
        prevepisode=findViewById(R.id.exo_prevvideo);
        qualityup=findViewById(R.id.qualityup);
            decorView = getWindow().getDecorView();
            uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
           decorView.setSystemUiVisibility(uiOptions);

            new Description(getApplicationContext()).execute();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            playerView = findViewById(R.id.exoplayer);
            playerView.setPlayer(simpleExoPlayer);
            datasourcefactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "tryingexoplayer"));

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        outState.putString("videolink",finallink);

        super.onSaveInstanceState(outState);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();

        }

    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
               | View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    private class Description extends AsyncTask<Void, Void, Void> {
        String desc,x;
        VideoView v;
        Context context;
        Description(Context context)
        {
            this.context=context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected Void doInBackground(Void... params) {
            qualitysetter=0;
            finallink=null;
            storinggoogleurls.clear();
            storingquality.clear();
            try {
                if(nextlink==null)
                 link = getIntent().getStringExtra("link");
                else
                    link=nextlink;
                Log.i("linkinglink",link);
                animename=getIntent().getStringExtra("animename");
                imagelink=getIntent().getStringExtra("imagelink");
                int gettingindex=link.lastIndexOf("-");
                epno=Integer.parseInt(link.substring(gettingindex+1,link.length()));
               s=Integer.parseInt(getIntent().getStringExtra("noofepisodes"));
                if(link.equals("https://www8.gogoanimes.tv/ansatsu-kyoushitsu-tv--episode-1"))
                    mBlogDocument=Jsoup.connect("https://www8.gogoanimes.tv/ansatsu-kyoushitsu-episode-1").get();
                else
                 mBlogDocument = Jsoup.connect(link).get();
                previousvideolink=mBlogDocument.select("div[class=anime_video_body_episodes_l]").select("a").attr("abs:href");
                nextvideolink=mBlogDocument.select("div[class=anime_video_body_episodes_r]").select("a").attr("abs:href");

                Elements mElementDataSize = mBlogDocument.select("iframe");
                Elements mElement = mBlogDocument.select("span[class=btndownload]");
                x = mElementDataSize.attr("src");
                if(mElementDataSize.size()==0)
                {
Elements elements=mBlogDocument.select("li[class=mp4]").select("a");

l=elements.attr("data-video");;
                }
             else{
                    l = "https:" + x;
                    String vidstreamlink=mElementDataSize.attr("src");
                    int abc=vidstreamlink.indexOf("id=");
                    int k=abc;
                    while(vidstreamlink.charAt(k)!='=')
                        k++;
                    k++;
                    while(vidstreamlink.charAt(k)!='&')
                        k++;
                     id=vidstreamlink.substring(abc,k);

                    String downloadlink="https://vidstream.co/download?"+id;

                    org.jsoup.nodes.Document reacheddownloadlink=Jsoup.connect(downloadlink).timeout(0).get();
                    Elements elements1 = reacheddownloadlink.select("div[class=dowload]").select("a");
                    while (elements1.eq(qualitysetter).attr("href").contains("googlevideo")
                          ||elements1.eq(qualitysetter).attr("href").contains("googleuser")
                            )
                    { storinggoogleurls.add(elements1.eq(qualitysetter).attr("href"));
                        String x=String.valueOf(elements1.eq(qualitysetter).text());
                        String c=new StringBuffer(x.substring(10,15)).toString();
                        storingquality.add(c);
                        qualitysetter++;}
                        qualitysetter--;

                        if (qualitysetter == -1) {
                            qualitysetter = 0;

                            org.jsoup.nodes.Document rapidvideo = Jsoup.connect(elements1.eq(qualitysetter).attr("href")).get();

                            Elements e = rapidvideo.select("div[class=video]");
                            if(e.size()>0)
                            {
                            Elements f = e.eq(e.size() - 1).select("span").select("a");
                            if(f.size()>0) {
                                qualitysetter=-1;
                                for (int m = 0; m < f.size(); m++) {
                                    storinggoogleurls.add(f.eq(m).attr("href"));

                                    String p=f.eq(m).select("span").html();
                                    int index=p.indexOf(" ");

                                    storingquality.add(p.substring(index+1,p.length()));
                                    qualitysetter++;
                                }
                                Log.i("checkingsoea",f.eq(qualitysetter).attr("href"));
                                finallink=f.eq(qualitysetter).attr("href");
                                current=qualitysetter;
                            }
                        }
                        }

                        else
                        {


                    finallink = elements1.eq(qualitysetter).attr("href");
                    current=qualitysetter;
                    Log.i("zxc", finallink);
                        } }

                } catch (IOException e1) {
                e1.printStackTrace();
            }

            if(finallink==null) {
                String rapid = mBlogDocument.select("li[class=rapidvideo]").select("a").attr("data-video");
                if (rapid != null&&rapid.contains("rapidvideo")) {
                    try

                    {

                        org.jsoup.nodes.Document scrapingrapidvideo = Jsoup.connect(rapid).get();
                        qualitysetter=-1;
                        String rapidvideolink = scrapingrapidvideo.select("video[id=videojs]").select("source").attr("src");
                        storinggoogleurls.add(rapidvideolink);
                        storingquality.add(scrapingrapidvideo.select("video[id=videojs]").select("source").attr("title"));
                        qualitysetter++;
                        current=qualitysetter;
                        finallink = rapidvideolink;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

                 if(finallink==null)
            {
                String vidcn=mBlogDocument.select("li[class=vidcdn]").select("a").attr("data-video");
                if(vidcn!=null&&vidcn.contains("load.php"))
                {
                    try
                    {
                        Document scrapingvidcdn=Jsoup.connect(vidcn).get();
                        String html=scrapingvidcdn.html();
                        int indexoffile=html.indexOf("file:");
                        String link="";
                        while(html.charAt(indexoffile)!='h')
                            indexoffile++;
                        while(html.charAt(indexoffile)!='\'')
                        {
                            link=link+html.charAt(indexoffile);
                            indexoffile++;
                        }
                        if(!link.contains("m3u8")) {
                            finallink = link;
                            qualitysetter=-1;
                            qualitysetter++;
                            current=qualitysetter;
                        storinggoogleurls.add(finallink);
                        storingquality.add("Unknown");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                    }
            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                Connection.Response response = null;
                try {
                    response = Jsoup.connect(finallink).followRedirects(false).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response != null) {
                    finallink=response.header("location");

                }

            }


            runOnUiThread(new Runnable() {
          @Override
          public void run() {
              title.setText(animename+" Episode "+epno);
              title.setVisibility(View.VISIBLE);


              if(finallink==null)
              {
                  Intent intent = new Intent(context, webvideo.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  intent.putExtra("videostreamlink", l);
                  startActivity(intent);
                  finish();
              }
              else{
                  MediaSource vediosource = new ExtractorMediaSource.Factory(datasourcefactory).createMediaSource(Uri.parse(finallink));
                  simpleExoPlayer.prepare(vediosource);

                  playerView.getPlayer().setPlayWhenReady(true);

                  qualitydown.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {

                          qualitysetter--;
                          if(qualitysetter<0)
                          { Toast.makeText(context,"Least quality",Toast.LENGTH_SHORT).show();
                              qualitysetter++;}
                          else
                          {
                              long t=playerView.getPlayer().getCurrentPosition();
                              MediaSource vediosource=    new ExtractorMediaSource.Factory(datasourcefactory).createMediaSource(Uri.parse(storinggoogleurls.get(qualitysetter)));
                              simpleExoPlayer.prepare(vediosource);
                              playerView.getPlayer().setPlayWhenReady(true);
                              playerView.getPlayer().seekTo(t);

                          }
                      }
                  });
                  qualityup.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {

                          qualitysetter++;
                          if(qualitysetter>=storinggoogleurls.size())
                          {
                              Toast.makeText(context,"Max Quality",Toast.LENGTH_SHORT).show();
                              qualitysetter--;
                          }
                          else
                          {
                              long t=playerView.getPlayer().getCurrentPosition();

                              MediaSource vediosource=    new ExtractorMediaSource.Factory(datasourcefactory).createMediaSource(Uri.parse(storinggoogleurls.get(qualitysetter)));
                              simpleExoPlayer.prepare(vediosource);
                              playerView.getPlayer().setPlayWhenReady(true);
                              playerView.getPlayer().seekTo(t);
                          }

                      }
                  });
                   final String[] a= storingquality.toArray(new String[0]);
                  qualitychanger.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          AlertDialog.Builder builder = new AlertDialog.Builder(WatchVideo.this,AlertDialog.THEME_HOLO_LIGHT);
                          builder.setTitle("Quality")
                                  .setItems(a, new DialogInterface.OnClickListener() {
                                      public void onClick(DialogInterface dialog, int which) {
                                          // The 'which' argument contains the index position
                                          // of the selected item
                                          if(current!=which)
                                          {
                                          long t=playerView.getPlayer().getCurrentPosition();

                                            current=which;
                                          MediaSource vediosource=    new ExtractorMediaSource.Factory(datasourcefactory).createMediaSource(Uri.parse(storinggoogleurls.get(which)));
                                          simpleExoPlayer.prepare(vediosource);
                                          playerView.getPlayer().setPlayWhenReady(true);
                                          playerView.getPlayer().seekTo(t);
                                      }}
                                  });
                          builder.show();
                      }
                  });

                  simpleExoPlayer.addListener(new Player.EventListener() {
                      @Override
                      public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                      }

                      @Override
                      public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                      }

                      @Override
                      public void onLoadingChanged(boolean isLoading) {

                      }

                      @Override
                      public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                       if(playbackState==ExoPlayer.STATE_ENDED)
                       {
                           int index=link.lastIndexOf("-");
                           episodeno=Integer.parseInt(link.substring(index+1,link.length()));
                           episodeno=episodeno+1;
                           if(nextvideolink==null||nextvideolink.equals(""))
                               Toast.makeText(getApplicationContext(),"Last Episode",Toast.LENGTH_SHORT).show();
                           else
                           {

                               nextlink=nextvideolink;
                               String z="'"+ animename+"','Episode "+episodeno+"','"+nextlink+"','"+imagelink+"'";
                               recent.execSQL("delete from anime where EPISODELINK='"+nextlink+"'");
simpleExoPlayer.stop();
                               recent.execSQL("INSERT INTO anime VALUES("+z+");");
                               new Description(getApplicationContext()).execute();
                           }
                       }
                          if (playbackState == ExoPlayer.STATE_BUFFERING){
                              progressBar.setVisibility(View.VISIBLE);
                          } else {
                              progressBar.setVisibility(View.INVISIBLE);
                          }
                      }

                      @Override
                      public void onRepeatModeChanged(int repeatMode) {

                      }

                      @Override
                      public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                      }

                      @Override
                      public void onPlayerError(ExoPlaybackException error) {
                          Toast.makeText(context, "Cannot play video trying other method", Toast.LENGTH_SHORT).show();
                          playerView.getPlayer().release();

                          Intent intent = new Intent(context, webvideo.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                          intent.putExtra("videostreamlink", l);
                          startActivity(intent);
                          finish();
                      }

                      @Override
                      public void onPositionDiscontinuity(int reason) {

                      }

                      @Override
                      public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                      }

                      @Override
                      public void onSeekProcessed() {

                      }
                  });
              }
          }
      });
            return null;
   }

        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            nextepisode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int index=link.lastIndexOf("-");
                     episodeno=Integer.parseInt(link.substring(index+1,link.length()));
                    episodeno=episodeno+1;
                    Log.i("nextvideolink",nextvideolink);
                   if(nextvideolink==null||nextvideolink.equals(""))
                        Toast.makeText(getApplicationContext(),"Last Episode",Toast.LENGTH_SHORT).show();
                    else
                    {

                        nextlink=nextvideolink;
                        String z="'"+ animename+"','Episode "+episodeno+"','"+nextlink+"','"+imagelink+"'";
                        recent.execSQL("delete from anime where EPISODELINK='"+nextlink+"'");

                        recent.execSQL("INSERT INTO anime VALUES("+z+");");
                        new Description(getApplicationContext()).execute();
                    }
                }
            });
            prevepisode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int index=link.lastIndexOf("-");
                    int episodeno=Integer.parseInt(link.substring(index+1,link.length()));

                    episodeno=episodeno-1;
                   if(previousvideolink==null||previousvideolink.equals(""))

                        Toast.makeText(getApplicationContext(),"First Episode",Toast.LENGTH_SHORT).show();
                    else
                    {

                        nextlink=previousvideolink;
                        String z="'"+ animename+"','Episode "+episodeno+"','"+nextlink+"','"+imagelink+"'";
                        recent.execSQL("delete from anime where EPISODELINK='"+nextlink+"'");
                        recent.execSQL("INSERT INTO anime VALUES("+z+");");
                        new Description(getApplicationContext()).execute();
                    }
                }
            });
        }
    }
@Override
public void onPause()
{
    super.onPause();
    time= playerView.getPlayer().getCurrentPosition();
    playerView.getPlayer().stop();

}

@Override
public  void  onResume()
{
    super.onResume();
    if(finallink!=null)
    {
        MediaSource vediosource=    new ExtractorMediaSource.Factory(datasourcefactory).createMediaSource(Uri.parse(finallink));
        simpleExoPlayer.prepare(vediosource);}
    playerView.getPlayer().setPlayWhenReady(true);
    playerView.getPlayer().seekTo(time);
}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            playerView.getPlayer().release();
            super.onBackPressed();
        }
        return false;
    }
}
