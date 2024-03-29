package it.unipi.sam.app.activities;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.DMRequestWrapper;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.DownloadBroadcastReceiver;
import it.unipi.sam.app.util.JacksonUtil;
import it.unipi.sam.app.util.MyBroadcastListener;
import it.unipi.sam.app.util.RestInfo;

public class DownloadActivity extends BasicActivity implements MyBroadcastListener {
    private static final String TAG = "AAADownloadActivity";

    //overall
    protected static final int REST_INFO_JSON = 0;
    //overview
    protected static final int OVERVIEW_INFO_JSON = 1;
    //news
    protected static final int NEWS_JSON = 2;

    protected DownloadBroadcastReceiver receiver;
    protected DownloadManager dm;
    protected ConcurrentHashMap<Long, Integer> idToResourceType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            restInfoInstance = getIntent().getParcelableExtra(Constants.rest_info_instance_key);
        }catch(Exception e){
            e.printStackTrace();
            restInfoInstance = null;
        }
        idToResourceType = new ConcurrentHashMap<>(); // fill it with ids of requests (takes count of resources which are requested from this context)
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new DownloadBroadcastReceiver(this);
        registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(receiver);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void enqueueRequest(DMRequestWrapper dmReq){
        long id = dm.enqueue(dmReq.getReq()); // long id
        //DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "enqueueRequest. id: "+id, null);
        idToResourceType.put(id, dmReq.getResourceType());
    }

    @Override
    public void onDownloadCompleted(long id) {
        DownloadManager.Query q = new DownloadManager.Query();
        q.setFilterById(id);
        Cursor c = dm.query(q);
        if (c.moveToFirst()) {
            int j = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int h = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
            int t = c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP); // note: if it fails returns -1.
            int status = c.getInt(j);
            int columnReason = c.getColumnIndex(DownloadManager.COLUMN_REASON);
            int reason = c.getInt(columnReason);
            String uriString = c.getString(h);
            Integer resources_type = idToResourceType.get(id);
            if(status == DownloadManager.STATUS_SUCCESSFUL){
                long lastModifiedTimestamp = c.getLong(t);
                if(resources_type==null) {
                    c.close();
                    return;
                }
                DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "onDownloadCompleted. "+resources_type+" ready: "+ uriString, null);
                handleResponseUri(id, resources_type, uriString, lastModifiedTimestamp, true);
                if(resources_type==REST_INFO_JSON) // rest info file content is in memory now and we don't want to keep the file
                    dm.remove(id); // delete last downloaded restInfo record from dm
            }else if(status == DownloadManager.STATUS_FAILED) {
                DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "download failed reason: " + reason, null);
                handle404(id, resources_type, uriString);
            }else if(status == DownloadManager.STATUS_PAUSED) {
                DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "download paused reason: " + reason, null);
            }else if(status == DownloadManager.STATUS_PENDING) {
                DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "download status: PENDING", null);
            }else if(status == DownloadManager.STATUS_RUNNING){
                DebugUtility.LogDThis(DebugUtility.SERVER_COMMUNICATION, TAG, "download status: RUNNING", null);
            }
        }
        c.close();
    }

    // override this in activities
    protected void handle404(long dm_resource_id, Integer type, String uriString) {}

    /**
     * Handle uri.
     * @param type type of the resource
     * @param uriString local uri as string
     * @param lastModifiedTimestamp last modified timestamp
     * @param updateResourcePreference set it to true if you are handling a just downloaded resource
     */
    // override this in activities (call super.handleResponseUri(...))
    protected void handleResponseUri(long dm_resource_id, Integer type, String uriString, long lastModifiedTimestamp, boolean updateResourcePreference) {
        if (type == REST_INFO_JSON) {
            // here after sending the first request. We got the rest service info.

            // get the file content
            String content = getFileContentFromUri(uriString);

            // perform jackson from file to object
            try {
                restInfoInstance =
                        (RestInfo) JacksonUtil.getObjectFromString(content, RestInfo.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
    }

    protected String getFileContentFromUri(String uriString) {
        InputStream is;
        try {
            is = getContentResolver().openInputStream(Uri.parse(uriString));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder content;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(is))){
            String line;
            content = new StringBuilder();
            while ((line = br.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                is.close();
            } catch (IOException ignored) {}
            return null;
        }

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return content.toString();
    }
}


