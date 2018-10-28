package com.stuffbox.webscraper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.jsoup.Jsoup;
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
    WebView webView;
    String finallink;
    ProgressDialog mProgressDialog;
    private ImageView imageView;
TextView qualityvalue;
    VideoView videoView;
    String l;
    long time;
    int k=0;
    int qualitysetter=0;
    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private PlayerView playerView;
    private ArrayList<String> storinggoogleurls=new ArrayList<>();
    private boolean mExoPlayerFullscreen = false;
    SimpleExoPlayer simpleExoPlayer;
    org.jsoup.nodes.Document mBlogDocument ;
    private int mResumeWindow;
    View decorView;
    ImageButton qualityup,qualitydown;
    int uiOptions;
    private  ArrayList<String> storingquality=new ArrayList<>();
    com.google.android.exoplayer2.upstream.DataSource.Factory datasourcefactory;
    private long mResumePosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       setContentView(R.layout.videoviewer);
         decorView = getWindow().getDecorView();
         uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        if(finallink!=null)
        Log.i("voiz",finallink);
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);


        }
qualitydown=findViewById(R.id.qualitydown);
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
            //First Hide other objects (listview or recyclerview), better hide them using Gone.

            DefaultBandwidthMeter bandwidthMeter1 = new DefaultBandwidthMeter();
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
        decorView.setSystemUiVisibility(uiOptions);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
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
     //   Description(VideoView videoView, Context context)
        Description(Context context)
        {
       //     v=videoView;
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
            try {
                // Connect to the web site
                String link = getIntent().getStringExtra("link");
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
                Log.i("mataana", String.valueOf(mElement.size()));
                x = mElementDataSize.attr("src");
                if(mElementDataSize.size()==0)
                {
Elements elements=mBlogDocument.select("li[class=mp4]").select("a");
//Log.i("printing size",String.valueOf(elements.size()));
String value=elements.attr("data-video");
//int index=value.indexOf("embed");
//Log.i("printingindex",String.valueOf(index));
//StringBuffer str=new StringBuffer(value);

    //str.replace(index,index+5,"watch");
  //                  Log.i("printingx",str.toString());

//Log.i("printing url",value);
                //    org.jsoup.nodes.Document mp4link=Jsoup.connect(value).get();
              //      Log.i("sizeofmp4link",String.valueOf(mp4link));
            //       Elements elements1=mp4link.select("div[id=player]");
               //     Log.i("printing url",elements1);
l=value;
                //    finallink=elements1;
                  //  Log.i("oneaaja",String.valueOf(elements1));
                }
             else{   try {
                    l = "https:" + x;
                    //      Log.i("Checkingsomethingsomething",l);
                    org.jsoup.nodes.Document vid = Jsoup.connect(l).get();
                    Log.i("videf", String.valueOf(vid));
                    Elements elements = vid.select("script").eq(5);
                    Elements xxa = vid.select("script");
                    Log.i("bhaichaljanaa", String.valueOf(xxa.size()));
                    Log.i("check", String.valueOf(elements));
                    String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
                    Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
                    Matcher urlMatcher = pattern.matcher(String.valueOf(elements));
                    ArrayList<String> containedUrls = new ArrayList<String>();
                    while (urlMatcher.find()) {
                        containedUrls.add(String.valueOf(elements).substring(urlMatcher.start(0),
                                urlMatcher.end(0)));
                    }
                    if (containedUrls.size() == 0) {
                        Elements x = vid.select("script").eq(3);
                        String a = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
                        Pattern z = Pattern.compile(a, Pattern.CASE_INSENSITIVE);
                        Matcher y = z.matcher(String.valueOf(x));
                        ArrayList<String> b = new ArrayList<String>();
                        while (y.find()) {
                            containedUrls.add(String.valueOf(x).substring(y.start(0),
                                    y.end(0)));
                        }
                    }
//Log.i("loghoja",String.valueOf(containedUrls.size()));
                  for (int i = 0; i < containedUrls.size(); i++)
                     Log.i("Checkblabla", containedUrls.get(i));
                 //Log.i("Checkblabla",containedUrls.get(3));
//if(containedUrls.size()==0)
                    //   Toast.makeText(context,"cannot play video",Toast.LENGTH_SHORT).show();
                    if(containedUrls.size()==0)
                    {
                        Elements elements2=mBlogDocument.select("li[class=mp4]").select("a");
//Log.i("printing size",String.valueOf(elements.size()));
                        String value=elements2.attr("data-video");
//int index=value.indexOf("embed");
//Log.i("printingindex",String.valueOf(index));
//StringBuffer str=new StringBuffer(value);

                        //str.replace(index,index+5,"watch");
                        //                  Log.i("printingx",str.toString());

//Log.i("printing url",value);
                        //    org.jsoup.nodes.Document mp4link=Jsoup.connect(value).get();
                        //      Log.i("sizeofmp4link",String.valueOf(mp4link));
                        //       Elements elements1=mp4link.select("div[id=player]");
                        //     Log.i("printing url",elements1);
                        l=value;
                    }
                else{    org.jsoup.nodes.Document videostreamlink = Jsoup.connect(containedUrls.get(containedUrls.size() - 1)).get();
                    if (String.valueOf(videostreamlink).contains("htttps://nl3.")) {
                        Log.i("chalrhahaiye", "firbhinhichalrha");
                        videostreamlink = Jsoup.connect(containedUrls.get(4)).get();
                    }

                    Log.i("blablablabla", String.valueOf(videostreamlink));
                    Elements elements1 = videostreamlink.select("div[class=dowload]").select("a");
                    Log.i("sizeof", String.valueOf(elements1.size()));
//Log.i("sahihaiyanhi",elements1.attr("href"));
                    while (elements1.eq(qualitysetter).attr("href").contains("googlevideo"))
                    { storinggoogleurls.add(elements1.eq(qualitysetter).attr("href"));
                   // storingquality.add(String.valueOf(elements1.eq(i).text()));
                        String x=String.valueOf(elements1.eq(qualitysetter).text());
                        String c=new StringBuffer(x.substring(10,15)).toString();
                        storingquality.add(c);
                        qualitysetter++;}
                        qualitysetter--;
                        Log.i("testing",String.valueOf(qualitysetter));

                        if (qualitysetter == -1) {
                       qualitysetter = 0;
                   //     qualitydown.setVisibility(View.GONE);
                   //     qualityup.setVisibility(View.GONE);
                   //     qualityvalue.setVisibility(View.GONE);
                            Log.i("NHI CHALA",elements1.eq(qualitysetter).attr("href"));
                            org.jsoup.nodes.Document rapidvideo=Jsoup.connect(elements1.eq(qualitysetter).attr("href")).get();
                        //    org.jsoup.nodes.Document d=Jsoup.connect("https://www.rapidvideo.com/d/FV6EZSZWKF").get();
                           // System.out.println(d.html());
                            Elements e=rapidvideo.select("div[class=video]");
                            Elements f=e.eq(e.size()-1).select("span").select("a");
            finallink=f.eq(f.size()-1).attr("href");
            Log.i("loggingrapidvideo",finallink);
                    }
                        else
                        {
                        qualityvalue=findViewById(R.id.qualityxy);


                    finallink = elements1.eq(qualitysetter).attr("href");
                    Log.i("sahihaiyanhi", elements1.eq(qualitysetter ).attr("href"));
for(int j=0;j<storingquality.size();j++)
{
    Log.i("loggingurl",storinggoogleurls.get(j));
    Log.i("loggingquality",storingquality.get(j));
}
                    Log.i("zxc", finallink);
                    Log.i("marjaao", String.valueOf(elements));} }
                }   catch (IOException e) {
                    e.printStackTrace();
                }
                }
                for(int i=0;i<mElementDataSize.size();i++)
                    Log.i("manik",mElementDataSize.eq(i).html());
                String videostreamlink=mElementDataSize.html();
                  Log.i("mani",String.valueOf(mBlogDocument));
                  Log.i("sda",x);
                Log.i("videostream",String.valueOf(mElementDataSize.size()));
                // Locate the content attribute
   }catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set description into TextView
mProgressDialog.dismiss();
       //     RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.act_recyclerview);
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
                        Log.i("loggingqualitysetter",String.valueOf(qualitysetter));

                        qualitysetter--;
                        if(qualitysetter<0)
                        { Toast.makeText(context,"Least quality",Toast.LENGTH_SHORT).show();
                        qualitysetter++;}
                        else
                        {
                            long t=playerView.getPlayer().getCurrentPosition();
                            Log.i("loggintime",String.valueOf(t));
                            Log.i("loggingurl",storinggoogleurls.get(qualitysetter));

                            //     playerView.getPlayer().release();
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
                        Log.i("loggingqualitysetter",String.valueOf(qualitysetter));

                        qualitysetter++;
                        if(qualitysetter==storinggoogleurls.size())
                        {
                            Toast.makeText(context,"Max Quality",Toast.LENGTH_SHORT).show();
                            qualitysetter--;
                        }
                        else
                        {
                            long t=playerView.getPlayer().getCurrentPosition();
                           // playerView.getPlayer().release();
                            Log.i("loggintime",String.valueOf(t));
Log.i("loggingurl",storinggoogleurls.get(qualitysetter));
                            MediaSource vediosource=    new ExtractorMediaSource.Factory(datasourcefactory).createMediaSource(Uri.parse(storinggoogleurls.get(qualitysetter)));
                            simpleExoPlayer.prepare(vediosource);
                            playerView.getPlayer().setPlayWhenReady(true);
                            playerView.getPlayer().seekTo(t);
                        }

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
        return true;
    }
}
