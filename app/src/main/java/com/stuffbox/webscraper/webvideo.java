package com.stuffbox.webscraper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webvideo extends Activity {
    String url;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.webviewer);
        webView = findViewById(R.id.website);
        url = getIntent().getStringExtra("videostreamlink");
        Log.i("checkingstring",url);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
       // webView.getScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);;
        webView.loadUrl(url);

        //  setContentView(webView);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
   //     Intent setIntent = new Intent(this,MainActivity.class);
        webView.goBack();

        //    setIntent.addCategory(Intent.CATEGORY_HOME);
    //    setIntent.putExtra("sentfromhere",1);
    //    setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //    startActivity(setIntent);
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //    view.loadUrl(url);
            return true;
        }
    }
}
