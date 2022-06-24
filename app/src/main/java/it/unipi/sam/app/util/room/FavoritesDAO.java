package it.unipi.sam.app.util.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import it.unipi.sam.app.util.FavoritesWrapper;

@Dao
public interface FavoritesDAO {
    @Insert
    void insertAll(FavoritesWrapper... favWrappers);

    @Delete
    void delete(FavoritesWrapper favoritesWrapper);

    @Query("SELECT * FROM favorites")
    List<FavoritesWrapper> getAll();

    /*@Query("SELECT news FROM favorites WHERE NULLIF(news, '') IS NOT NULL")
    public List<VCNews> getNews();

    @Query("SELECT team FROM favorites WHERE NULLIF(team, '') IS NOT NULL")
    public List<Map<String, Team>> getTeams();

    @Query("SELECT person FROM favorites WHERE NULLIF(person, '') IS NOT NULL")
    public List<Map<String, Person>> getPeople();*/
}
