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
                try {
                    List<FavoritesWrapper> l = (List<FavoritesWrapper>) msg.obj;
                    rfl.onFavoritesRetrived(l);
                }catch(ClassCastException e){
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void run() {
        // ottengo preferiti
        FavoritesDAO favoritesDAO = mDb.favoritesDAO();
        List<FavoritesWrapper> favorites = favoritesDAO.getAll();

        Message msg = new Message();
        msg.obj = favorites;
        mHandler.sendMessage(msg);
    }
}
