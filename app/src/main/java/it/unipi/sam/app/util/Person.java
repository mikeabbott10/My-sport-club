package it.unipi.sam.app.util;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Person implements Parcelable {
    private String tag;
    private String name;
    private String role;
    private int number;
    private String nation;
    private int birthYear;
    private int height;
    private Map<String,String> team;
    private ArrayList<String> social;

    protected Person(Parcel in) {
        tag = in.readString();
        name = in.readString();
        role = in.readString();
        number = in.readInt();
        nation = in.readString();
        birthYear = in.readInt();
        height = in.readInt();

        final int teamMapLength = in.readInt(); // size of team map
        team = new HashMap<>();
        for (int j = 0; j < teamMapLength; j++) {
            team.put(in.readString(), in.readString());
        }

        social = in.createStringArrayList();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);
        parcel.writeString(name);
        parcel.writeString(role);
        parcel.writeInt(number);
        parcel.writeString(nation);
        parcel.writeInt(birthYear);
        parcel.writeInt(height);

        parcel.writeInt(team.size()); // size of team map
        for (Map.Entry<String, String> entry : team.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue());
        }

        parcel.writeStringList(social);
    }

    @NonNull
    @Override
    public String toString() {
        return "Person{" +
                "tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
