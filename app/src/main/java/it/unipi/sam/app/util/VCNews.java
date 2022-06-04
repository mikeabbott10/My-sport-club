package it.unipi.sam.app.util;

import java.io.Serializable;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VCNews implements Serializable {
    private String tag;
    private String title;
    private String author;
    private long date;
    private String resourcePath;
    private String logoImgName;
    private String coverImgName;
    private String imageDescription;
    private String description;
    private String relatedGameName;
    private String relatedGameHomeSets;
    private String relatedGameAwaySets;

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

}
