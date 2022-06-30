package it.unipi.sam.app.util;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VCNews implements Parcelable, Comparable<VCNews>{
    private String tag;
    private String title;
    private String author;
    public long date;
    private long id;
    private String resourcePath;
    private String logoImgName;
    private String coverImgName;
    private String imageDescription;
    private String description;
    private String relatedGameName;
    private String relatedGameHomeSets;
    private String relatedGameAwaySets;

    protected VCNews(Parcel in) {
        tag = in.readString();
        title = in.readString();
        author = in.readString();
        date = in.readLong();
        id = in.readLong();
        resourcePath = in.readString();
        logoImgName = in.readString();
        coverImgName = in.readString();
        imageDescription = in.readString();
        description = in.readString();
        relatedGameName = in.readString();
        relatedGameHomeSets = in.readString();
        relatedGameAwaySets = in.readString();
    }

    public static final Creator<VCNews> CREATOR = new Creator<VCNews>() {
        @Override
        public VCNews createFromParcel(Parcel in) {
            return new VCNews(in);
        }

        @Override
        public VCNews[] newArray(int size) {
            return new VCNews[size];
        }
    };

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }

    public String getResourcePath() {
        return resourcePath;
    }
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getLogoImgName() {
        return logoImgName;
    }
    public void setLogoImgName(String logoImgName) {
        this.logoImgName = logoImgName;
    }

    public String getCoverImgName() {
        return coverImgName;
    }
    public void setCoverImgName(String coverImgName) {
        this.coverImgName = coverImgName;
    }

    public String getImageDescription() {
        return imageDescription;
    }
    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelatedGameName() {
        return relatedGameName;
    }
    public void setRelatedGameName(String relatedGameName) {
        this.relatedGameName = relatedGameName;
    }

    public String getRelatedGameHomeSets() {
        return relatedGameHomeSets;
    }
    public void setRelatedGameHomeSets(String relatedGameHomeSets) {
        this.relatedGameHomeSets = relatedGameHomeSets;
    }

    public String getRelatedGameAwaySets() {
        return relatedGameAwaySets;
    }
    public void setRelatedGameAwaySets(String relatedGameAwaySets) {
        this.relatedGameAwaySets = relatedGameAwaySets;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int compareTo(VCNews vcNews) {
        return this.date > vcNews.getDate() ? -1 : 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeLong(date);
        parcel.writeLong(id);
        parcel.writeString(resourcePath);
        parcel.writeString(logoImgName);
        parcel.writeString(coverImgName);
        parcel.writeString(imageDescription);
        parcel.writeString(description);
        parcel.writeString(relatedGameName);
        parcel.writeString(relatedGameHomeSets);
        parcel.writeString(relatedGameAwaySets);
    }

    @NonNull
    @Override
    public String toString() {
        return "VCNews{" +
                "tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                ", id=" + id +
                '}';
    }
}
