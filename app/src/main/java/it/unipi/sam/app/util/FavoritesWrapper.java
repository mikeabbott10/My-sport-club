package it.unipi.sam.app.util;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity(tableName = "favorites") //, ignoredColumns = {"FAVORITE_NEWS", "FAVORITE_TEAM", "FAVORITE_PERSON"})
public class FavoritesWrapper implements Parcelable {
    @Ignore
    public static final int FAVORITE_NEWS = 0;
    @Ignore
    public static final int FAVORITE_TEAM = 1;
    @Ignore
    public static final int FAVORITE_PERSON = 2;

    @PrimaryKey(autoGenerate = true)
    public int id;

    private VCNews news;
    private Team team;
    private Person person;

    @Ignore
    public FavoritesWrapper(VCNews news) {
        this.news = news;
        this.team = null;
        this.person = null;
    }
    @Ignore
    public FavoritesWrapper(Team team) {
        this.team = team;
        this.news = null;
        this.person = null;
    }
    @Ignore
    public FavoritesWrapper(Person person) {
        this.person = person;
        this.news = null;
        this.team = null;
    }

    protected FavoritesWrapper(Parcel in) {
        id = in.readInt();
        news = in.readParcelable(VCNews.class.getClassLoader());
    }

    public static final Creator<FavoritesWrapper> CREATOR = new Creator<FavoritesWrapper>() {
        @Override
        public FavoritesWrapper createFromParcel(Parcel in) {
            return new FavoritesWrapper(in);
        }

        @Override
        public FavoritesWrapper[] newArray(int size) {
            return new FavoritesWrapper[size];
        }
    };

    public int getInstance(){
        if(news!=null)
            return FAVORITE_NEWS;
        if(team!=null)
            return FAVORITE_TEAM;
        if(person!=null)
            return FAVORITE_PERSON;
        assert false;
        return -1;
    }

    public VCNews getNews() {
        return news;
    }
    public void setNews(VCNews news) {
        this.news = news;
    }

    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }

    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeParcelable(news, i);
    }
}
