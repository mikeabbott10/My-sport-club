package it.unipi.sam.app.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VCNews {
    private String tag;
    private String title;
    private String author;
    private long date;
    private String logoPath;
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

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
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
