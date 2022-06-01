package it.unipi.sam.app.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DownloadBroadcastReceiver extends BroadcastReceiver {
    private final MyBroadcastListener listener;

    public DownloadBroadcastReceiver(MyBroadcastListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("DownloadBroadcastReceiver - Action: " + intent.getAction() + "\n"+
                "URI: " + intent.toUri(Intent.URI_INTENT_SCHEME) + "\n");
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,0);
            // qui gestiamo il completamento del download
            listener.onDownloadCompleted(id);
        }
    }
}