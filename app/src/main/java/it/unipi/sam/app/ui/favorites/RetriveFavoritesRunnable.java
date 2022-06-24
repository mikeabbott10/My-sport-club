package it.unipi.sam.app.ui.favorites;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import it.unipi.sam.app.util.FavoritesWrapper;
import it.unipi.sam.app.util.room.AppDatabase;
import it.unipi.sam.app.util.room.FavoritesDAO;

public class RetriveFavoritesRunnable implements Runnable {
    private final Handler mHandler;
    private final AppDatabase mDb;

    public RetriveFavoritesRunnable(RetriveFavoritesListener rfl, AppDatabase db) {
        this.mDb = db;
        mHandler = new Handler(Looper.getMainLooper()){
            @Override public void handleMessage(@NonNull Message msg) {
                if(rfl == null){
                    return;
                }
                Log.d("TAGGHE", msg.obj.toString());
                rfl.onFavoritesRetrived((List<FavoritesWrapper>) msg.obj);
            }
        };
    }

    @Override
    public void run() {
        // ottengo preferiti
        FavoritesDAO favoritesDAO = mDb.favoritesDAO();
        List<FavoritesWrapper> favorites = favoritesDAO.getAll();
        sendMessageToHandler(0,0,favorites);
    }

    private void sendMessageToHandler(int arg1, int arg2, Object obj){
        Message incomingMessage = new Message();
        incomingMessage.arg1 = arg1;
        incomingMessage.arg2 = arg2;
        incomingMessage.obj = obj;
        mHandler.sendMessage(incomingMessage);
    }
}
