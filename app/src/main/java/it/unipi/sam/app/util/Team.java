package it.unipi.sam.app.util;

import java.util.ArrayList;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Team {
    private ArrayList<String> players;
    private String coach;
    private String secondCoach;
    private String assistantManager;
    private String currentLeague;
    private String leagueDescription;
    private String leagueLink;

    public String getCurrentLeague() {
        return currentLeague;
    }
    public void setCurrentLeague(String currentLeague) {
        this.currentLeague = currentLeague;
    }

    public String getCoach() {
        return coach;
    }
    public void setCoach(String coach) {
        this.coach = coach;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }
    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public String getSecondCoach() {
        return secondCoach;
    }
    public void setSecondCoach(String secondCoach) {
        this.secondCoach = secondCoach;
    }

    public String getAssistantManager() {
        return assistantManager;
    }
    public void setAssistantManager(String assistantManager) {
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
