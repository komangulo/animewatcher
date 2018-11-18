package com.stuffbox.webscraper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
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
import org.jsoup.helper.HttpConnection;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.net.URL;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jasbe on 22-08-2018.
 */

public class WatchVideo extends Activity {
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
    int siz,epno;
    View decorView;
    ImageButton qualityup,qualitydown;
    int uiOptions;
    String animename,imagelink;
    private  ArrayList<String> storingquality=new ArrayList<>();
    com.google.android.exoplayer2.upstream.DataSource.Factory datasourcefactory;
    private long mResumePosition;
    ImageButton qualitychanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       setContentView(R.layout.videoviewer);
        recent=openOrCreateDatabase("recent",MODE_PRIVATE,null);
         decorView = getWindow().getDecorView();
         uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
               | View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        title=findViewById(R.id.titleofanime);
        title.setVisibility(View.GONE);
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
        //nextepisode.setEnabled(true);
        prevepisode=findViewById(R.id.exo_prevvideo);
        qualityup=findViewById(R.id.qualityup);
            decorView = getWindow().getDecorView();
            uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
           decorView.setSystemUiVisibility(uiOptions);

            new Description(getApplicationContext()).execute();
            Handler handler = new Handler();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            playerView = findViewById(R.id.exoplayer);
            playerView.setPlayer(simpleExoPlayer);
        //    DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
            datasourcefactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "tryingexoplayer"));

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        Log.i("loggingsomething",finallink);
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
                //| View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE;
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
            mProgressDialog = new ProgressDialog(WatchVideo.this);
            mProgressDialog.setTitle("Video");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            finallink=null;
            try {
                if(nextlink==null)
                 link = getIntent().getStringExtra("link");
                else
                    link=nextlink;
                animename=getIntent().getStringExtra("animename");
              //  animelink=link;
                imagelink=getIntent().getStringExtra("imagelink");
          //      siz=Integer.parseInt(getIntent().getStringExtra("size"));
                int gettingindex=link.lastIndexOf("-");
                 epno=Integer.parseInt(link.substring(gettingindex+1,link.length()));
                Log.i("templog",String.valueOf(epno));
           //    s=Integer.parseInt(getIntent().getStringExtra("noofepisodes"));
                  Log.i("templog",String.valueOf(s));
                if(link.equals("https://www8.gogoanimes.tv/ansatsu-kyoushitsu-tv--episode-1"))
                    mBlogDocument=Jsoup.connect("https://www8.gogoanimes.tv/ansatsu-kyoushitsu-episode-1").get();
                else
                 mBlogDocument = Jsoup.connect(link).get();
                Log.i("blabla", link);

                //    Log.i("soja",String.valueOf(mBlogDocument));
                // Using Elements to get the Meta data
                //      Elements mElementDataSize = mBlogDocument.select("div[class=author-date]");
                Elements mElementDataSize = mBlogDocument.select("iframe");
                Elements mElement = mBlogDocument.select("span[class=btndownload]");
              //  Log.i("mataana", String.valueOf(mElement.size()));
                x = mElementDataSize.attr("src");
                if(mElementDataSize.size()==0)
                {
Elements elements=mBlogDocument.select("li[class=mp4]").select("a");
String value=elements.attr("data-video");

l=value;
                }
             else{
              //Deleted Code Comes here
                    l = "https:" + x;
                    String vidstreamlink=mElementDataSize.attr("src");
                  //  System.out.println(vidstreamlink);
                    int abc=vidstreamlink.indexOf("id=");
                    int k=abc;
                    while(vidstreamlink.charAt(k)!='=')
                        k++;
                    k++;
                    while(vidstreamlink.charAt(k)!='=')
                        k++;
                    String sub=vidstreamlink.substring(abc,k);

                    String downloadlink="https://vidstream.co/download?"+sub;
                    org.jsoup.nodes.Document reacheddownloadlink=Jsoup.connect(downloadlink).timeout(0).get();
                  //  Elements gettingdownloadlink=reacheddownloadlink.select("div[class=dowload]").select("a");
                    Elements elements1 = reacheddownloadlink.select("div[class=dowload]").select("a");
        //            Log.i("sizeof", String.valueOf(elements1.size()));
//Log.i("sahihaiyanhi",elements1.attr("href"));
                    while (elements1.eq(qualitysetter).attr("href").contains("googlevideo")
                          ||elements1.eq(qualitysetter).attr("href").contains("googleuser")
                            )
                    { storinggoogleurls.add(elements1.eq(qualitysetter).attr("href"));
                   // storingquality.add(String.valueOf(elements1.eq(i).text()));
                        String x=String.valueOf(elements1.eq(qualitysetter).text());
                        String c=new StringBuffer(x.substring(10,15)).toString();
                        storingquality.add(c);
                        qualitysetter++;}
                        qualitysetter--;
      //                  Log.i("testing",String.valueOf(qualitysetter));

                        if (qualitysetter == -1) {
                            qualitysetter = 0;

    //                        Log.i("NHI CHALA", elements1.eq(qualitysetter).attr("href"));
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
                            //Log.i("loggingrapidvideo", finallink);
                        }
                        }

                        else
                        {


                    finallink = elements1.eq(qualitysetter).attr("href");
                    current=qualitysetter;
  //                  Log.i("sahihaiyanhi", elements1.eq(qualitysetter ).attr("href"));
//for(int j=0;j<storingquality.size();j++)
//{
  //  Log.i("loggingurl",storinggoogleurls.get(j));
   // Log.i("loggingquality",storingquality.get(j));
//}
                    Log.i("zxc", finallink);
                        } }
             //   }   catch (IOException e) {
            //        e.printStackTrace();
            //    }
                } catch (IOException e1) {
                e1.printStackTrace();
            }
            if(finallink==null) {
                String rapid = mBlogDocument.select("li[class=rapidvideo]").select("a").attr("data-video");
               // Log.i("linkis", rapid);
                if (rapid != null) {
                    try

                    {
                        org.jsoup.nodes.Document scrapingrapidvideo = Jsoup.connect(rapid).get();
                        String rapidvideolink = scrapingrapidvideo.select("video[id=videojs]").select("source").attr("src");
                        finallink = rapidvideolink;
                     //   Log.i("rapidvideolink is ", rapidvideolink);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                // only for marshmellow and lower versions
                Connection.Response response = null;
                try {
                    response = Jsoup.connect(finallink).followRedirects(false).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response != null) {
               //     Log.i("loggingredirectedlink",response.header("location"));
                    finallink=response.header("location");

                }

            }


            runOnUiThread(new Runnable() {
          @Override
          public void run() {
              title.setText(animename+" Episode "+epno);
              title.setVisibility(View.VISIBLE);
              mProgressDialog.dismiss();
              //     RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.act_recyclerview);

//finallink="https://r6---sn-cvh76n7k.googlevideo.com/videoplayback?id=36560fbb45884d3a&itag=37&source=picasa&begin=0&requiressl=yes&mm=30&mn=sn-cvh76n7k&ms=nxu&mv=m&pl=24&sc=yes&ei=_D_sW72LN8HR4QKq14bACA&susc=ph&app=fife&mime=video/mp4&cnr=14&dur=900.075&lmt=1542190445346413&mt=1542209444&title=&ip=1.186.111.17&ipbits=8&expire=1542216732&sparams=ip,ipbits,expire,id,itag,source,requiressl,mm,mn,ms,mv,pl,sc,ei,susc,app,mime,cnr,dur,lmt&signature=57EA640DE18D469F12C469A699109F3D70E567A72A3A1A49B20CDDADFF816E61.17A323B0C27E5D26EFB96CD4DB6409867ECF7CDF72A7646B500DA63E680A4171&key=us0";

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
                  //    videoView.setVideoURI(Uri.parse(finallink));

                  playerView.getPlayer().setPlayWhenReady(true);

                  qualitydown.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                         // Log.i("loggingqualitysetter",String.valueOf(qualitysetter));

                          qualitysetter--;
                          if(qualitysetter<0)
                          { Toast.makeText(context,"Least quality",Toast.LENGTH_SHORT).show();
                              qualitysetter++;}
                          else
                          {
                              long t=playerView.getPlayer().getCurrentPosition();
                        //      Log.i("loggintime",String.valueOf(t));
                        //      Log.i("loggingurl",storinggoogleurls.get(qualitysetter));

                              //     playerView.getPlayer().release();
                              //          simpleExoPlayer.stop();
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
                   //       Log.i("loggingqualitysetter",String.valueOf(qualitysetter));

                          qualitysetter++;
                          if(qualitysetter>=storinggoogleurls.size())
                          {
                              Toast.makeText(context,"Max Quality",Toast.LENGTH_SHORT).show();
                              qualitysetter--;
                          }
                          else
                          {
                              long t=playerView.getPlayer().getCurrentPosition();
                              // playerView.getPlayer().release();
                         //     Log.i("loggintime",String.valueOf(t));
//Log.i("loggingurl",storinggoogleurls.get(qualitysetter));
                              //        simpleExoPlayer.stop();

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
                          AlertDialog.Builder builder = new AlertDialog.Builder(WatchVideo.this);
                          builder.setTitle("Quality")
                                  .setItems(a, new DialogInterface.OnClickListener() {
                                      public void onClick(DialogInterface dialog, int which) {
                                          // The 'which' argument contains the index position
                                          // of the selected item
                                          if(current!=which)
                                          {
                                          long t=playerView.getPlayer().getCurrentPosition();
                                          // playerView.getPlayer().release();
                                          //     Log.i("loggintime",String.valueOf(t));
//Log.i("loggingurl",storinggoogleurls.get(qualitysetter));
                                          //        simpleExoPlayer.stop();
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
                       //    Log.i("episodecurrent",String.valueOf(episodeno));
                           episodeno=episodeno+1;
                           if(episodeno>siz)
                               Toast.makeText(getApplicationContext(),"Last Episode",Toast.LENGTH_SHORT).show();
                           else
                           {
                               //   Log.i("episodeafterchange",String.valueOf(x));
                               nextlink = link.substring(0, index + 1);
                               nextlink = nextlink + episodeno;
                               String z="'"+ animename+"','Episode "+episodeno+"','"+nextlink+"','"+imagelink+"'";
                           //    Log.i("loggingsql",z);
                               recent.execSQL("delete from anime where EPISODELINK='"+nextlink+"'");
simpleExoPlayer.stop();
                               recent.execSQL("INSERT INTO anime VALUES("+z+");");
                           //    Log.i("nextlinkis", nextlink);
                               new Description(getApplicationContext()).execute();
                           }
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
            String  searchurl="https://www04.gogoanimes.tv//search.html?keyword="+animename;
            try {
                org.jsoup.nodes.Document searching = Jsoup.connect(searchurl).get();
                Elements elements=searching.select("div[class=main_body]").select("div[class=last_episodes]").select("ul[class=items]").select("li");
         //       Log.i("haiyanhi",String.valueOf(elements.size()));
                for(int i=0;i<elements.size();i++)
                {
                    String animelink=elements.select("div[class=img]").eq(i).select("a").attr("abs:href");
                    String anime=elements.select("div[class=img]").eq(i).select("a").attr("title");
                    if(anime.equals(animename))
                        break;
                   // String imagelink=elements.select("div[class=img]").eq(i).select("img").attr("src");


                 //   Log.i("working",animelink);
                }
                 searching = Jsoup.connect(link).get();
             //   Log.i("zyx",String.valueOf(searching));
                 elements=searching.select("div[class=anime_video_body]").select("ul[id=episode_page]").select("li");
             //   Log.i("checkinga",String.valueOf(elements.size()));
             //   for(int i=0;i<elements.size();i++)
           //         Log.i("ptanhikya",String.valueOf(elements.select("a").eq(i).html()));
                String a=String.valueOf(elements.select("a").eq(elements.size()-1).html());
                StringBuffer b=new StringBuffer();
           //     Log.i("ptanhikya",String.valueOf(a));
                for(int i=0;i<a.length();i++)
                {
                    if(a.charAt(i)=='-')
                    {
                        for(int j=i+1;j<a.length();j++)
                            b.append(a.charAt(j));
                    }
                }
               siz= Integer.parseInt(b.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
   }
   //catch (IOException e) {
     //           e.printStackTrace();
     //       }
        //    return null;
      //  }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView
            nextepisode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int index=link.lastIndexOf("-");
                     episodeno=Integer.parseInt(link.substring(index+1,link.length()));
              //      Log.i("episodecurrent",String.valueOf(episodeno));
                    episodeno=episodeno+1;
                    if(episodeno>siz)
                        Toast.makeText(getApplicationContext(),"Last Episode",Toast.LENGTH_SHORT).show();
                    else
                    {
                        //   Log.i("episodeafterchange",String.valueOf(x));
                        nextlink = link.substring(0, index + 1);
                        nextlink = nextlink + episodeno;
                        String z="'"+ animename+"','Episode "+episodeno+"','"+nextlink+"','"+imagelink+"'";
                   //     Log.i("loggingsql",z);
                        recent.execSQL("delete from anime where EPISODELINK='"+nextlink+"'");

                        recent.execSQL("INSERT INTO anime VALUES("+z+");");
                   //     Log.i("nextlinkis", nextlink);
                        new Description(getApplicationContext()).execute();
                    }
                }
            });
            prevepisode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int index=link.lastIndexOf("-");
                    int episodeno=Integer.parseInt(link.substring(index+1,link.length()));
                  //  Log.i("episodecurrent",String.valueOf(episodeno));
                    episodeno=episodeno-1;
                    if(episodeno<1)
                        Toast.makeText(getApplicationContext(),"First Episode",Toast.LENGTH_SHORT).show();
                    else
                    {
                        //   Log.i("episodeafterchange",String.valueOf(x));
                        nextlink = link.substring(0, index + 1);
                        nextlink = nextlink + episodeno;
                        String z="'"+ animename+"','Episode "+episodeno+"','"+nextlink+"','"+imagelink+"'";
                   //     Log.i("loggingsql",z);
                        recent.execSQL("delete from anime where EPISODELINK='"+nextlink+"'");
                        recent.execSQL("INSERT INTO anime VALUES("+z+");");
                   //     Log.i("nextlinkis", nextlink);
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
