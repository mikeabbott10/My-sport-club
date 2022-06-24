package it.unipi.sam.app.util.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import it.unipi.sam.app.util.FavoritesWrapper;

@Database(entities = {FavoritesWrapper.class}, version = 1)
@TypeConverters({FavoritesConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoritesDAO favoritesDAO();
}