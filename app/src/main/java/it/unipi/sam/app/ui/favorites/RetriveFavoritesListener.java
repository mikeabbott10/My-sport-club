package it.unipi.sam.app.ui.favorites;

import java.util.List;

import it.unipi.sam.app.util.FavoritesWrapper;

public interface RetriveFavoritesListener {
    void onFavoritesRetrived(List<FavoritesWrapper> favorites);
}
