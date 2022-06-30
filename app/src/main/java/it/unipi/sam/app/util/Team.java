package it.unipi.sam.app.util;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Team implements Parcelable {
    private String tag;
    private ArrayList<Map<String, String>> players;
    private Map<String, String> coach;
    private Map<String, String> secondCoach;
    private ArrayList<Map<String, String>> assistantManager;
    private String currentLeague;
    private String leagueDescription;
    private String leagueLink;

    protected Team(Parcel in) {
        tag = in.readString();

        players = new ArrayList<>();
        final int playersListSize = in.readInt(); // size of arraylist
        for (int i = 0; i < playersListSize; i++) {
            final int mapLength = in.readInt(); // size of i-th map
            Map<String, String> m = new HashMap<>();
            for (int j = 0; j < mapLength; j++) {
                m.put(in.readString(), in.readString());
            }
            players.add(m);
        }

        final int coachMapLength = in.readInt(); // size of coach map
        coach = new HashMap<>();
        for (int j = 0; j < coachMapLength; j++) {
            coach.put(in.readString(), in.readString());
        }

        final int secondCoachMapLength = in.readInt(); // size of coach map
        secondCoach = new HashMap<>();
        for (int j = 0; j < secondCoachMapLength; j++) {
            secondCoach.put(in.readString(), in.readString());
        }

        assistantManager = new ArrayList<>();
        final int amListSize = in.readInt(); // size of arraylist
        for (int i = 0; i < amListSize; i++) {
            final int mapLength = in.readInt(); // size of i-th map
            Map<String, String> m = new HashMap<>();
            for (int j = 0; j < mapLength; j++) {
                m.put(in.readString(), in.readString());
            }
            assistantManager.add(m);
        }

        currentLeague = in.readString();
        leagueDescription = in.readString();
        leagueLink = in.readString();
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);

        parcel.writeInt(players.size()); // size of arraylist
        for (Map<String,String> m: players) {
            parcel.writeInt(m.size()); // size of i-th map
            for (Map.Entry<String, String> entry : m.entrySet()) {
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }
        }

        parcel.writeInt(coach.size()); // size of coach map
        for (Map.Entry<String, String> entry : coach.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue());
        }

        parcel.writeInt(secondCoach.size()); // size of secondCoach map
        for (Map.Entry<String, String> entry : secondCoach.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue());
        }

        parcel.writeInt(assistantManager.size()); // size of arraylist
        for (Map<String,String> m: assistantManager) {
            parcel.writeInt(m.size()); // size of i-th map
            for (Map.Entry<String, String> entry : m.entrySet()) {
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }
        }

        parcel.writeString(currentLeague);
        parcel.writeString(leagueDescription);
        parcel.writeString(leagueLink);
    }

    @NonNull
    @Override
    public String toString() {
        return "Team{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
