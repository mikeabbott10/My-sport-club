package it.unipi.sam.app.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import com.google.android.material.snackbar.Snackbar;

public class DebugUtility {
    //debug
    private static final String TAG = "AAAADebugUtility";

    public static final int ERROR_LOG = 0;
    public static final int AUTH_LOG = 1;
    public static final int BASIC_IDENTITY_LOG = 2;
    public static final int IDENTITY_LOG = 3;
    public static final int SERVER_COMMUNICATION = 4;
    public static final int UI_RELATED_LOG = 5;
    public static final int TOUCH_OR_CLICK_RELATED_LOG = 6;
    public static final int ORIENTED_TO_UI_LOGIC_LOG = 7;
    public static final int BROADCAST_RECEIVER = 9;
    public static final int RECORDING_SCREEN_LOG = 10;
    public static final int FILE_HANDLING_LOG = 11;
    public static final int TCP_CLASS_RELATED_LOG = 12;
    public static final int CACHE_LOG = 13;
    public static final int MYBOY_COMMUNICATION = 14;
    public static final int FIREBASE_RELATED_LOG = 15;

    private static final boolean showAuthenticationLog = false;
    private static final boolean showBasicLog = false;
    private static final boolean showIdentityLog = true;
    private static final boolean showServerCommunicationLog = true;
    private static final boolean showUILog = true;
    private static final boolean showGestureLog = false;
    private static final boolean showRecordingScreenLog = false;
    private static final boolean showMyFileHandling = true;
    private static final boolean showTcpLog = false;
    private static final boolean showCacheLog = false;
    private static final boolean showMyBoyCommunicationRelatedLog = false;
    private static final boolean showFirebaseRelatedLog = false;

    private static boolean debugEnabled = true;

    public static void LogDThis(int type, String TAG, String msg, Context context){
        if(debugEnabled && iCanLog(type)) {
            //writeClearFile(context, " DEBUG: " + msg);
            Log.d(TAG, msg!=null? msg : "DEBUGUTILITY ADVICE: msg is null");
        }
    }
    public static void LogWThis(int type, String TAG, String msg, Context context){
        if(debugEnabled && iCanLog(type)) {
            //writeClearFile(context, " WARNING: " + msg);
            Log.w(TAG,msg);
        }
    }
    public static void LogEThis(int type, String TAG, String msg, Context context){
        if(debugEnabled && iCanLog(type)) {
            //writeClearFile(context, " ERROR: " + msg);
            Log.e(TAG,msg);
        }
    }

    private static boolean iCanLog(int type) {
        if(type == AUTH_LOG && showAuthenticationLog)
            return true;
        else if(type == BASIC_IDENTITY_LOG && showBasicLog)
            return true;
        else if(type == IDENTITY_LOG && showIdentityLog)
            return true;
        else if(type == SERVER_COMMUNICATION && showServerCommunicationLog)
            return true;
        else if((type == UI_RELATED_LOG || type == ORIENTED_TO_UI_LOGIC_LOG) && showUILog)
            return true;
        else if(type == TOUCH_OR_CLICK_RELATED_LOG && showGestureLog)
            return true;
        else if(type == BROADCAST_RECEIVER)
            return true;
        else if(type == RECORDING_SCREEN_LOG && showRecordingScreenLog)
            return true;
        else if(type == FILE_HANDLING_LOG && showMyFileHandling)
            return true;
        else if(type == TCP_CLASS_RELATED_LOG && showTcpLog)
            return true;
        else if(type == CACHE_LOG && showCacheLog)
            return true;
        else if(type == MYBOY_COMMUNICATION && showMyBoyCommunicationRelatedLog)
            return true;
        else if(type == FIREBASE_RELATED_LOG && showFirebaseRelatedLog)
            return true;
        else
            return (type == ERROR_LOG);
    }

    public static void showSimpleSnackbar(View view, String msg, int duration){
        Snackbar.make(view, msg, duration).show();
    }

    private static void writeClearFile(Context ctx, String stringToWrite) {
        //Log.d(TAG, " > writeNonEncryptedFile");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ctx.checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                return;
        }
        File myDir = new File(Environment.getExternalStorageDirectory() + "/Remote_Linking");
        if(!myDir.exists()){
            if(!myDir.mkdir()){
                Log.d(TAG, "Can't create folder");
                return;
            }
        }
        File logFile = new File(myDir.getPath()+"/logFile.txt");
        stringToWrite = new Date().toString() + stringToWrite;
        stringToWrite = stringToWrite + "\n";
        try{
            //Log.d(TAG, "Writing in file: " + file.toString() + " . Text: " + stringToWrite);
            FileOutputStream fos = new FileOutputStream(logFile, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(stringToWrite);
            osw.flush();
            osw.close();
        } catch (IOException e) {
            Log.d(TAG,  logFile.toString() + " errore in scrittura");
        }
    }
}