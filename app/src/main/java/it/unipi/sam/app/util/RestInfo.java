package it.unipi.sam.app.util;

import java.io.Serializable;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestInfo implements Serializable {
    private String news;
    private String peoplePath;
    private String teamsPath;
    private String[] teamCodes;
    private Map<String, String> keyWords;
    private Map<String, Map<String, Object>> lastModified;

    public Map<String, Map<String, Object>> getLastModified() {
        return lastModified;
    }
    public void setLastModified(Map<String, Map<String, Object>> lastModified) {
        this.lastModified = lastModified;
    }

    public String getNews() {
        return news;
    }
    public void setNews(String news) {
        this.news = news;
    }

    public String getPeoplePath() {
        return peoplePath;
    }
    public void setPeoplePath(String peoplePath) {
        this.peoplePath = peoplePath;
    }

    public String getTeamsPath() {
        return teamsPath;
    }
    public void setTeamsPath(String teamsPath) {
        this.teamsPath = teamsPath;
    }

    public String[] getTeamCodes() {
        return teamCodes;
    }
    public void setTeamCodes(String[] teamCodes) {
        this.teamCodes = teamCodes;
    }

    public Map<String, String> getKeyWords() {
        return keyWords;
    }
    public void setKeyWords(Map<String, String> keyWords) {
        this.keyWords = keyWords;
    }
}
