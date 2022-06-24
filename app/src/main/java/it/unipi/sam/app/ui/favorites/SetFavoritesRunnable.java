package it.unipi.sam.app.ui.favorites;

import java.util.List;

import it.unipi.sam.app.util.FavoritesWrapper;
import it.unipi.sam.app.util.room.AppDatabase;
import it.unipi.sam.app.util.room.FavoritesDAO;

public class SetFavoritesRunnable implements Runnable {
    private final AppDatabase mDb;
    private final FavoritesWrapper favWrapper;

    public SetFavoritesRunnable(AppDatabase db, FavoritesWrapper favWrapper) {
        this.mDb = db;
        this.favWrapper = favWrapper;
    }

    @Override
    public void run() {
        FavoritesDAO favoritesDAO = mDb.favoritesDAO();
        // ottengo preferiti
        List<FavoritesWrapper> l = favoritesDAO.getAll();
        if(l.contains(favWrapper))
            favoritesDAO.delete(favWrapper);
        else
            favoritesDAO.insertAll(favWrapper);
    }
}