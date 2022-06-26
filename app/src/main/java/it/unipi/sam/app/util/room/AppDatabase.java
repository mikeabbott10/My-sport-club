package it.unipi.sam.app.util.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.FavoritesWrapper;

@Database(entities = {FavoritesWrapper.class}, version = 1)
@TypeConverters({FavoritesConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract FavoritesDAO favoritesDAO();

    // singleton pattern
    public static AppDatabase getDatabase(Context context) {
        if (instance == null) {
            instance =
                    Room.databaseBuilder(context, AppDatabase.class, Constants.database_name)
                            .addTypeConverter(new FavoritesConverter()).build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}