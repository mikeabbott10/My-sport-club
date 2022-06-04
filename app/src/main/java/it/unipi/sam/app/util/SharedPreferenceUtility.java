package it.unipi.sam.app.util;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import static android.content.Context.MODE_PRIVATE;

import it.unipi.sam.app.R;

public class SharedPreferenceUtility {
    private static final String TAG = "CLCLSharedPreference";

    // general resources
    public static ResourcePreferenceWrapper getResourceUri(Context ctx, String key, Long last_update_timestamp) {
        SharedPreferences prefs = ctx.getSharedPreferences(key, MODE_PRIVATE);
        String uri = prefs.getString(key, null);
        long last_modified = prefs.getLong(key+ctx.getString(R.string.timestamp), -1);

        // se last_update_timestamp è nullo bypasso il confronto tra timestamp
        if(last_update_timestamp != null) {
            if(last_modified!=-1 && last_modified <= last_update_timestamp)
                return null;
        }

        long dm_resource_id = prefs.getLong(key+ctx.getString(R.string.id), -1);
        if(isValidUri(ctx, uri, last_modified)) {
            return new ResourcePreferenceWrapper(uri, last_modified, dm_resource_id);
        }
        return null;
    }
    public static void setResourceUri(Context ctx, String key, String uri, long timestamp, long dm_resource_id) {
        SharedPreferences.Editor spEditor = ctx.getSharedPreferences(key, MODE_PRIVATE).edit();
        spEditor.putString(key, uri);
        spEditor.putLong(key+ctx.getString(R.string.timestamp), timestamp);
        spEditor.putLong(key+ctx.getString(R.string.id), dm_resource_id);
        spEditor.apply();
    }

    // Team resources
    /*public static ResourcePreferenceWrapper getTeamResourceUri(Context ctx, String teamCode, int type, Long last_update_timestamp) {
        SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.teams)+teamCode+type, MODE_PRIVATE);
        String uri = prefs.getString(ctx.getString(R.string.teams)+teamCode+type, null);
        long last_modified = prefs.getLong(ctx.getString(R.string.teams)+ctx.getString(R.string.timestamp)+teamCode+type, -1);

        // se last_update_timestamp è nullo bypasso il confronto tra timestamp
        if(last_update_timestamp != null) {
            if (last_modified != -1 && last_modified <= last_update_timestamp)
                return null;
        }

        long dm_resource_id = prefs.getLong(ctx.getString(R.string.teams)+ctx.getString(R.string.id)+teamCode+type, -1);
        if(isValidUri(ctx, uri, last_modified))
            return new ResourcePreferenceWrapper(uri, last_modified, dm_resource_id);
        return null;
    }
    public static void setTeamResourceUri(Context ctx, String teamCode, int type, String uri, long timestamp, long dm_resource_id) {
        SharedPreferences.Editor spEditor = ctx.getSharedPreferences(ctx.getString(R.string.teams)+teamCode+type, MODE_PRIVATE).edit();
        spEditor.putString(ctx.getString(R.string.teams)+teamCode+type, uri);
        spEditor.putLong(ctx.getString(R.string.teams)+ctx.getString(R.string.timestamp)+teamCode+type, timestamp);
        spEditor.putLong(ctx.getString(R.string.teams)+ctx.getString(R.string.id)+teamCode+type, dm_resource_id);
        spEditor.apply();
    }*/

    public static boolean isValidUri(Context ctx, String contentUri, long last_modified){
        if(contentUri==null || last_modified==-1)
            return false;

        ContentResolver cr = ctx.getContentResolver();
        String[] projection = {DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP};
        Cursor cur = null;
        try {
            cur = cr.query(Uri.parse(contentUri), projection, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    long timestamp = cur.getLong(0);
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
            // api 23 doesn't allow the query and throws:
            // IllegalArgumentException: column last_modified_timestamp is not allowed in queries
        }finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }


}