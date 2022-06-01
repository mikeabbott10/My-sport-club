package it.unipi.sam.app.util;

import java.io.Serializable;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestInfo implements Serializable {
    private String news;
    private String playersPath;
    private String teamsPath;
    private String[] teamCodes;
    private Map<String, String> keyWords;
    private Map<String, Map<String, Long>> lastModifiedTimestamp;

    public Map<String, Map<String, Long>> getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }
    public void setLastModifiedTimestamp(Map<String, Map<String, Long>> lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public String getNews() {
        return news;
    }
    public void setNews(String news) {
        this.news = news;
    }

    public String getPlayersPath() {
        return playersPath;
    }
    public void setPlayersPath(String playersPath) {
        this.playersPath = playersPath;
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
