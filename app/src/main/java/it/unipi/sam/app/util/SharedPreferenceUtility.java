package it.unipi.sam.app.util;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import androidx.preference.PreferenceManager;

public class SharedPreferenceUtility {
    private static final String TAG = "CLCLSharedPreference";

    // show contacts popup
    public static boolean getNightMode(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(Constants.nightmode_key, false);
    }
    public static void setNightMode(Context ctx, boolean nightMode) {
        SharedPreferences.Editor spEditor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        spEditor.putBoolean(Constants.nightmode_key, nightMode);
        spEditor.apply();
    }

    // show contacts popup
    public static boolean getDontShowContactsPopup(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(Constants.dontshowcontactspopup_key, false);
    }
    public static void setDontShowContactsPopup(Context ctx, boolean dontShowPopup) {
        SharedPreferences.Editor spEditor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        spEditor.putBoolean(Constants.dontshowcontactspopup_key, dontShowPopup);
        spEditor.apply();
    }

    // show screen at start
    public static boolean getDontAskForDomainVerification(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        //SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.dontaskfordomainverification_key), MODE_PRIVATE);
        return prefs.getBoolean(Constants.dontaskfordomainverification_key, false);
    }
    public static void setDontAskForDomainVerification(Context ctx, boolean dontShowAtStartup) {
        SharedPreferences.Editor spEditor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        //SharedPreferences.Editor spEditor = ctx.getSharedPreferences(ctx.getString(R.string.dontaskfordomainverification_key), MODE_PRIVATE).edit();
        spEditor.putBoolean(Constants.dontaskfordomainverification_key, dontShowAtStartup);
        spEditor.apply();
    }

    // resources
    public static ResourcePreferenceWrapper getResourceUri(Context ctx, String key, Long last_update_timestamp) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        //SharedPreferences prefs = ctx.getSharedPreferences(key, MODE_PRIVATE);
        String uri = prefs.getString(key, null);
        long last_modified = prefs.getLong(key+Constants.timestamp_key, -1);

        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "last_modified:" + last_modified + ". uri:"+uri, null);

        // se last_update_timestamp è nullo bypasso il confronto tra timestamp
        if(last_update_timestamp != null) {
            if(last_modified!=-1 && last_modified <= last_update_timestamp)
                return null;
        }

        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "after timestamp check", null);

        long dm_resource_id = prefs.getLong(key+Constants.id_key, -1);
        if(isValidUri(ctx, uri, last_modified)) {
            return new ResourcePreferenceWrapper(uri, last_modified, dm_resource_id);
        }
        return null;
    }
    public static void setResourceUri(Context ctx, String key, String uri, long timestamp, long dm_resource_id) {
        SharedPreferences.Editor spEditor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        //SharedPreferences.Editor spEditor = ctx.getSharedPreferences(key, MODE_PRIVATE).edit();
        spEditor.putString(key, uri);
        spEditor.putLong(key+Constants.timestamp_key, timestamp);
        spEditor.putLong(key+Constants.id_key, dm_resource_id);
        spEditor.apply();
    }

    public static boolean isValidUri(Context ctx, String contentUri, long last_modified){
        if(contentUri==null || last_modified==-1)
            return false;

        ContentResolver cr = ctx.getContentResolver();
        // String[] projection = {DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP}; // IllegalArgumentException: column last_modified_timestamp is not allowed in queries below Android 8(?)
        Cursor cur = null;
        try {
            cur = cr.query(Uri.parse(contentUri), null, null, null, null);
            if (cur != null) {
                /*Log.d(TAG, "listing column for uri=" + contentUri);
                for (int i = 0; i<cur.getColumnCount(); i++) {
                    Log.d(TAG, "column " + i + "=" + cur.getColumnName(i));

                }*/
                if (cur.moveToFirst()) {
                    /*
                      Please note: "lastmod" is the name of the column which a DownloadManager.Query is
                      able to reach looking for the name DowloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP.
                      This is just a workaround because api 25- don't allow column last_modified_timestamp in queries
                     */
                    int last_modification_timestampIndex = cur.getColumnIndex("lastmod");

                    long timestamp = cur.getLong(last_modification_timestampIndex);
                    //Log.d(TAG, "timestamp:"+ timestamp);
                    if (timestamp == last_modified) {
                        cur.close();
                        return true;
                    }
                } else {
                    // Uri was ok but no entry found.
                }
            } else {
                // content Uri was invalid or some other error occurred
            }
        }catch(IllegalArgumentException | SecurityException ignored){
            // api 25- don't allow the query and throws:
            // IllegalArgumentException: column last_modified_timestamp is not allowed in queries
        }finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }


}