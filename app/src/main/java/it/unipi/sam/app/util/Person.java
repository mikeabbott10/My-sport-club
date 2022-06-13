package it.unipi.sam.app.util;

import java.util.ArrayList;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Person {
    public String name;
    public String role;
    public int number;
    public String nation;
    public int birthYear;
    public int height;
    public Map<String,String> team;
    public ArrayList<String> social;

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
