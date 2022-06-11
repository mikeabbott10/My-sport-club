package it.unipi.sam.app.util;

import java.util.ArrayList;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Team {
    private ArrayList<Map<String, String>> players;
    private Map<String, String> coach;
    private Map<String, String> secondCoach;
    private ArrayList<Map<String, String>> assistantManager;
    private String currentLeague;
    private String leagueDescription;
    private String leagueLink;

    public String getCurrentLeague() {
        return currentLeague;
    }
    public void setCurrentLeague(String currentLeague) {
        this.currentLeague = currentLeague;
    }

    public Map<String, String> getCoach() {
        return coach;
    }
    public void setCoach(Map<String, String> coach) {
        this.coach = coach;
    }

    public ArrayList<Map<String, String>> getPlayers() {
        return players;
    }
    public void setPlayers(ArrayList<Map<String, String>> players) {
        this.players = players;
    }

    public Map<String, String> getSecondCoach() {
        return secondCoach;
    }
    public void setSecondCoach(Map<String, String> secondCoach) {
        this.secondCoach = secondCoach;
    }

    public ArrayList<Map<String, String>> getAssistantManager() {
        return assistantManager;
    }
    public void setAssistantManager(ArrayList<Map<String, String>> assistantManager) {
        this.assistantManager = assistantManager;
    }

    public String getLeagueLink() {
        return leagueLink;
    }
    public void setLeagueLink(String leagueLink) {
        this.leagueLink = leagueLink;
    }

    public String getLeagueDescription() {
        return leagueDescription;
    }
    public void setLeagueDescription(String leagueDescription) {
        this.leagueDescription = leagueDescription;
    }
}
