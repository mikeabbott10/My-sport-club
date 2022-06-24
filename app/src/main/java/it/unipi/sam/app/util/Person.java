package it.unipi.sam.app.util;

import java.util.ArrayList;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Person {
    private String tag;
    private String name;
    private String role;
    private int number;
    private String nation;
    private int birthYear;
    private int height;
    private Map<String,String> team;
    private ArrayList<String> social;

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public String getNation() {
        return nation;
    }
    public void setNation(String nation) {
        this.nation = nation;
    }

    public int getBirthYear() {
        return birthYear;
    }
    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public Map<String, String> getTeam() {
        return team;
    }
    public void setTeam(Map<String, String> team) {
        this.team = team;
    }

    public ArrayList<String> getSocial() {
        return social;
    }
    public void setSocial(ArrayList<String> social) {
        this.social = social;
    }
}
