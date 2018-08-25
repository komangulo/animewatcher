package com.stuffbox.webscraper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

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
    VideoView videoView;
    int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.videoviewer);
         videoView=findViewById(R.id.videoview);
     //   new Description(videoView,getApplicationContext()).execute();
        new Description(getApplicationContext()).execute();
   //     WebView browser = (WebView) findViewById(R.id.webview);
      //  browser.loadUrl("http://www.tutorialspoint.com");
       //      DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
     //   android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoView.getLayoutParams();
     ///   params.width =  metrics.widthPixels;
     //  params.height = metrics.heightPixels;
    //    params.leftMargin = 0;
    //    videoView.setLayoutParams(params);
        MediaController mediaController = new MediaController(this);
       mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setMediaController(mediaController);
        videoView.start();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();

        }
    }
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
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
       /*     mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Anime");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show(); */
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
           String link=     getIntent().getStringExtra("link");
                org.jsoup.nodes.Document mBlogDocument = Jsoup.connect(link).get();
              Log.i("blabla",link);
            //    Log.i("soja",String.valueOf(mBlogDocument));
                // Using Elements to get the Meta data
                //      Elements mElementDataSize = mBlogDocument.select("div[class=author-date]");
                Elements mElementDataSize=mBlogDocument.select("iframe");
                Elements mElement=mBlogDocument.select("span[class=btndownload]");
                Log.i("mataana",String.valueOf(mElement.size()));
                x=mElementDataSize.attr("src");
                try{
                    String l="https:"+x;
                    org.jsoup.nodes.Document vid=Jsoup.connect(l).get();
                    Log.i("videf",String.valueOf(vid));
                    Elements elements=vid.select("script").eq(5);
                    Elements xxa=vid.select("script");
                    Log.i("bhaichaljanaa",String.valueOf(xxa.size()));
                    Log.i("checkblabla",String.valueOf(elements));
                    String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
                    Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
                    Matcher urlMatcher = pattern.matcher(String.valueOf(elements));
                    ArrayList<String> containedUrls = new ArrayList<String>();
                    while (urlMatcher.find())
                    {
                        containedUrls.add(String.valueOf(elements).substring(urlMatcher.start(0),
                                urlMatcher.end(0)));
                    }
                    if(containedUrls.size()==0)
                    {
                        Elements x=vid.select("script").eq(3);
                        String a = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
                        Pattern z = Pattern.compile(a, Pattern.CASE_INSENSITIVE);
                        Matcher y = z.matcher(String.valueOf(x));
                        ArrayList<String> b = new ArrayList<String>();
                        while (y.find())
                        {
                            containedUrls.add(String.valueOf(x).substring(y.start(0),
                                    y.end(0)));
                        }
                    }

                    for(int i=0;i<containedUrls.size();i++)
                       Log.i("Checkblabla",containedUrls.get(i));
                    //Log.i("Checkblabla",containedUrls.get(3));

                    org.jsoup.nodes.Document videostreamlink=Jsoup.connect(containedUrls.get(3)).get();
                    if(String.valueOf(videostreamlink).contains("htttps://nl3.")) {
                        Log.i("chalrhahaiye","firbhinhichalrha");
                        videostreamlink = Jsoup.connect(containedUrls.get(4)).get();
                    }
            /*        if(containedUrls.get(3).contains(".m3u8"))
                    {
                        Toast.makeText(context,"Cannot play video",Toast.LENGTH_SHORT);
                        Intent intent=new Intent(context,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    } */
Log.i("blablablabla",String.valueOf(videostreamlink));
Elements elements1= videostreamlink.select("div[class=dowload]").select("a");
Log.i("sizeof",String.valueOf(elements1.size()));
Log.i("sahihaiyanhi",elements1.attr("href"));
 finallink= elements1.attr("href");
 Log.i("zxc",finallink);                    Log.i("marjaao",String.valueOf(elements));
                }catch (IOException e){
                    e.printStackTrace();
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

            RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.act_recyclerview);
            videoView.setVideoURI(Uri.parse(finallink));
            final View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
                       videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    videoView.start();
                }
            });
                       videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                           @Override
                           public boolean onError(MediaPlayer mp, int what, int extra) {
              //           Intent intent=new Intent(context,M.class);
     //                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       //                        startActivity(intent);
////WebView webView=findViewById(R.id.as);
                     //          WebSettings webSettings=webView.getSettings();
                     //          webSettings.setJavaScriptEnabled(true);
                      //         videoView.setVisibility(View.INVISIBLE);
                      //         webView.setVisibility(View.VISIBLE);
                      //         webView.loadUrl("https://www.mp4upload.com/embed-yt5fgeu7ldxd.html");
                             Toast.makeText(context,"Cannot play video",Toast.LENGTH_SHORT).show();
                     //          ((ViewGroup)videoView.getParent()).removeView(videoView);
                     //          ((ViewGroup)webView.getParent()).removeView(webView);

                        //       LinearLayout layout=findViewById(R.id.webview);
                      //         layout.addView(webView);
                 //             setContentView(webView);
finish();
                               return false;
                           }
                       });
        }
    }

}
