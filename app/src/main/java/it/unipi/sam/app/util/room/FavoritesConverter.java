package it.unipi.sam.app.util.room;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.unipi.sam.app.util.JacksonUtil;
import it.unipi.sam.app.util.Person;
import it.unipi.sam.app.util.Team;
import it.unipi.sam.app.util.VCNews;

@ProvidedTypeConverter
public class FavoritesConverter {
    @TypeConverter
    public VCNews StringToVCNews(String string) {
        if(string==null) return null;
        // perform jackson from string to object
        try {
            return (VCNews) JacksonUtil.getObjectFromString(string, VCNews.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    @TypeConverter
    public String VCNewsToString(VCNews news) {
        if(news==null) return null;
        try {
            return JacksonUtil.getStringFromObject(news);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public Team StringToTeam(String string) {
        if(string==null) return null;
        // perform jackson from string to object
        try {
            return (Team) JacksonUtil.getObjectFromString(string, Team.class);
        } catch (JsonProcessingException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }
    @TypeConverter
    public String TeamToString(Team team) {
        if(team==null) return null;
        try {
            return JacksonUtil.getStringFromObject(team);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public Person StringToPerson(String string) {
        if(string==null) return null;
        // perform jackson from string to object
        try {
            return (Person) JacksonUtil.getObjectFromString(string, Person.class);
        } catch (JsonProcessingException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }
    @TypeConverter
    public String PersonToString(Person person) {
        if(person==null) return null;
        try {
            return JacksonUtil.getStringFromObject(person);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    /*@TypeConverter
    public FavoritesWrapper StringToFavoritesWrapper(String string) {
        if(string==null) return null;
        // perform jackson from string to object
        try {
            return (FavoritesWrapper) JacksonUtil.getObjectFromString(string, FavoritesWrapper.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    @TypeConverter
    public String FavoritesWrapperToString(FavoritesWrapper favWrap) {
        if(favWrap==null) return null;
        try {
            return JacksonUtil.getStringFromObject(favWrap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }*/
}