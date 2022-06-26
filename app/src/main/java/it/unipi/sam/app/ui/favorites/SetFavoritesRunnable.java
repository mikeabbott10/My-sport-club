package it.unipi.sam.app.ui.favorites;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import it.unipi.sam.app.util.FavoritesWrapper;
import it.unipi.sam.app.util.room.AppDatabase;
import it.unipi.sam.app.util.room.FavoritesDAO;

public class SetFavoritesRunnable implements Runnable {
    private final AppDatabase mDb;
    private final FavoritesWrapper favWrapper;
    private final Handler mHandler;
    public static final int DELETED = 0;
    public static final int INSERTED = 1;

    public SetFavoritesRunnable(AppDatabase db, FavoritesWrapper favWrapper, SetFavoritesListener sfl, Object item) {
        this.mDb = db;
        this.favWrapper = favWrapper;
        mHandler = new Handler(Looper.getMainLooper()){
            @Override public void handleMessage(@NonNull Message msg) {
                if(sfl == null){
                    return;
                }
                sfl.onFavoritesSetted(item, msg.arg1);
            }
        };
    }

    @Override
    public void run() {
        FavoritesDAO favoritesDAO = mDb.favoritesDAO();

        // se avessi in memoria favWrapper invece di istanziarlo in costruzione di this potrei:
        /*int affectedRows = favoritesDAO.delete( favWrapper );
        if(affectedRows<=0)
            favoritesDAO.insertAll(favWrapper);*/

        // ottengo preferiti
        Message msg = new Message();
        List<FavoritesWrapper> l = favoritesDAO.getAll();
        int i = l.indexOf(favWrapper);
        if(i != -1) {
            favoritesDAO.delete( l.get(i) );
            msg.arg1 = DELETED;
        }else{
            favoritesDAO.insertAll(favWrapper);
            msg.arg1 = INSERTED;
        }
        mHandler.sendMessage(msg);
    }
}