package it.unipi.sam.app.activities.overview;

import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Person {
    public String name;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String role;
    public Map<String, String> social;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getSocial() {
        return social;
    }
    public void setSocial(Map<String, String> social) {
        this.social = social;
    }
}
