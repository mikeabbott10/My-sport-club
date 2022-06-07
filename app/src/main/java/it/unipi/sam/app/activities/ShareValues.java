package it.unipi.sam.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import it.unipi.sam.app.R;


public class ShareValues extends Activity {
    //private static final String TAG = "AAAShareValues";
    public static final int SHARE_NEWS_PURPOSE = 1;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int activityPurpose = getIntent().getIntExtra("PURPOSE",0);
        if (activityPurpose == SHARE_NEWS_PURPOSE) {
            long news_id = getIntent().getLongExtra(getString(R.string.news_id),-1);
            shareMe("https", "volleycecina.it", "news", Long.toString(news_id));
        }//else
            //Log.d(TAG,"no valid extra");
        //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        setContentView(R.layout.transparent_layout);
        finish();
    }
    private void shareMe(String scheme, String host, String path, String resource_id){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_news_presentation_text));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, scheme + "://" + host + "/" + path + "/" + resource_id);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }
}