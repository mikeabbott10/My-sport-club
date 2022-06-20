package it.unipi.sam.app.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestInfo implements Parcelable {
    private String news;
    private String peoplePath;
    private String teamsPath;
    private ArrayList<Map<String,String>> femaleTeamTags;
    private ArrayList<Map<String,String>> maleTeamTags;
    private Map<String, String> keyWords;
    private Map<String, Map<String, Object>> lastModified;


    protected RestInfo(Parcel in) {
        news = in.readString();
        peoplePath = in.readString();
        teamsPath = in.readString();

        femaleTeamTags = new ArrayList<>();
        final int femaleListSize = in.readInt(); // size of arraylist
        for (int i = 0; i < femaleListSize; i++) {
            final int mapLength = in.readInt(); // size of i-th map
            Map<String, String> m = new HashMap<>();
            for (int j = 0; j < mapLength; j++) {
                m.put(in.readString(), in.readString());
            }
            femaleTeamTags.add(m);
        }

        maleTeamTags = new ArrayList<>();
        final int maleListSize = in.readInt(); // size of arraylist
        for (int i = 0; i < maleListSize; i++) {
            final int mapLength = in.readInt(); // size of i-th map
            Map<String, String> m = new HashMap<>();
            for (int j = 0; j < mapLength; j++) {
                m.put(in.readString(), in.readString());
            }
            maleTeamTags.add(m);
        }

        final int keyWordsMapLength = in.readInt(); // size of keywords map
        keyWords = new HashMap<>();
        for (int j = 0; j < keyWordsMapLength; j++) {
            keyWords.put(in.readString(), in.readString());
        }

        final int lastModifiedMapLength = in.readInt(); // size of lastModified map
        lastModified = new HashMap<>();
        for (int i = 0; i < lastModifiedMapLength; i++) {
            final String key = in.readString();
            Map<String, Object> ithMap = new HashMap<>();
            final int ithMapSize = in.readInt(); // size of i-th map
            for (int j = 0; j < ithMapSize; j++) {
                final String internalMapKey = in.readString();
                final int internalMapValueType = in.readInt();
                if(internalMapValueType == Constants.TIMESTAMP)
                    ithMap.put(internalMapKey, in.readLong());
                else if(internalMapValueType == Constants.PATH)
                    ithMap.put(internalMapKey, in.readString());
            }
            lastModified.put(key, ithMap);
        }

    }

    public static final Creator<RestInfo> CREATOR = new Creator<RestInfo>() {
        @Override
        public RestInfo createFromParcel(Parcel in) {
            return new RestInfo(in);
        }

        @Override
        public RestInfo[] newArray(int size) {
            return new RestInfo[size];
        }
    };

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

    public ArrayList<Map<String, String>> getFemaleTeamTags() {
        return femaleTeamTags;
    }
    public void setFemaleTeamTags(ArrayList<Map<String, String>> femaleTeamTags) {
        this.femaleTeamTags = femaleTeamTags;
    }

    public ArrayList<Map<String, String>> getMaleTeamTags() {
        return maleTeamTags;
    }
    public void setMaleTeamTags(ArrayList<Map<String, String>> maleTeamTags) {
        this.maleTeamTags = maleTeamTags;
    }

    public Map<String, String> getKeyWords() {
        return keyWords;
    }
    public void setKeyWords(Map<String, String> keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(news);
        parcel.writeString(peoplePath);
        parcel.writeString(teamsPath);

        parcel.writeInt(femaleTeamTags.size()); // size of arraylist
        for (Map<String,String> m: femaleTeamTags) {
            parcel.writeInt(m.size()); // size of i-th map
            for (Map.Entry<String, String> entry : m.entrySet()) {
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }
        }

        parcel.writeInt(maleTeamTags.size()); // size of arraylist
        for (Map<String,String> m: maleTeamTags) {
            parcel.writeInt(m.size()); // size of i-th map
            for (Map.Entry<String, String> entry : m.entrySet()) {
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }
        }

        parcel.writeInt(keyWords.size()); // size of keyWords map
        for (Map.Entry<String, String> entry : keyWords.entrySet()) {
            parcel.writeString(entry.getKey());
            parcel.writeString(entry.getValue());
        }

        parcel.writeInt(lastModified.size()); // size of lastModified map
        for (Map.Entry<String, Map<String,Object>> entry : lastModified.entrySet()) {
            parcel.writeString(entry.getKey());
            Map<String,Object> ithMap = entry.getValue();
            parcel.writeInt(ithMap.size()); // size of i-th map
            for (Map.Entry<String, Object> en : ithMap.entrySet()) {
                parcel.writeString(en.getKey());
                if(en.getValue() instanceof String){
                    parcel.writeInt(Constants.PATH);
                    parcel.writeString((String) en.getValue());
                }else if(en.getValue() instanceof Long){
                    parcel.writeInt(Constants.TIMESTAMP);
                    parcel.writeLong((Long) en.getValue());
                }
            }
        }

    }
}
