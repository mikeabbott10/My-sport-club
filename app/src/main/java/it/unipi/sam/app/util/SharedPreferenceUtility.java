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
    public static ResourcePreferenceWrapper getResourceUri(Context ctx, int type) {
        SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.general)+type, MODE_PRIVATE);
        String uri = prefs.getString(ctx.getString(R.string.general)+type, null);
        long last_modified = prefs.getLong(ctx.getString(R.string.general)+ctx.getString(R.string.timestamp)+type, -1);
        long dm_resource_id = prefs.getLong(ctx.getString(R.string.general)+ctx.getString(R.string.id)+type, -1);
        if(isValidUri(ctx, uri, last_modified)) {
            return new ResourcePreferenceWrapper(uri, last_modified, dm_resource_id);
        }
        return null;
    }
    public static void setResourceUri(Context ctx, int type, String uri, long timestamp, long dm_resource_id) {
        SharedPreferences.Editor spEditor = ctx.getSharedPreferences(ctx.getString(R.string.general)+type, MODE_PRIVATE).edit();
        spEditor.putString(ctx.getString(R.string.general)+type, uri);
        spEditor.putLong(ctx.getString(R.string.general)+ctx.getString(R.string.timestamp)+type, timestamp);
        spEditor.putLong(ctx.getString(R.string.general)+ctx.getString(R.string.id)+type, dm_resource_id);
        spEditor.apply();
    }

    // Team resources
    public static ResourcePreferenceWrapper getTeamResourceUri(Context ctx, String teamCode, int type) {
        SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.teams)+teamCode+type, MODE_PRIVATE);
        String uri = prefs.getString(ctx.getString(R.string.teams)+teamCode+type, null);
        long last_modified = prefs.getLong(ctx.getString(R.string.teams)+ctx.getString(R.string.timestamp)+teamCode+type, -1);
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
    }

    public static boolean isValidUri(Context ctx, String contentUri, long last_modified){
        if(contentUri==null || last_modified==-1)
            return false;

        ContentResolver cr = ctx.getContentResolver();
        String[] projection = {DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP};
        Cursor cur;
        try {
            cur = cr.query(Uri.parse(contentUri), projection, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    long timestamp = cur.getLong(0);
                    if (timestamp == last_modified) {
                        return true;
                    }
                } else {
                    // Uri was ok but no entry found.
                }
                cur.close();
            } else {
                // content Uri was invalid or some other error occurred
            }
        }catch(SecurityException ignored){}
        return false;
    }


}