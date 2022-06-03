package it.unipi.sam.app.util;

import java.util.ArrayList;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Team {
    private String currentLeague;
    private String coach;
    private ArrayList<String> players;

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
}
