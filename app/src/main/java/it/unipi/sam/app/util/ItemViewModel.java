package it.unipi.sam.app.util;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemViewModel extends ViewModel {
    private final SavedStateHandle savedStateHandle;
    private static final String currentFragmentKey = "cfk";
    private static final String femaleTeamsKey = "ftk";
    private static final String maleTeamsKey = "mtk";
    private static final String newsKey = "nk";
    private static final String favoritesKey = "fk";

    public ItemViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    public MutableLiveData<String> getSelectedFragmentName(){
        // Get the LiveData, setting the default value if it doesn't already have a value set.
        return savedStateHandle.getLiveData(currentFragmentKey, "");
    }
    public void selectFragmentName(String item){
        savedStateHandle.set(currentFragmentKey, item);
    }

    public MutableLiveData<List<Map<String,String>>> getFemaleTeamsList(){
        // Get the LiveData, setting the default value if it doesn't already have a value set.
        return savedStateHandle.getLiveData(femaleTeamsKey, new ArrayList<>());
    }
    public void setFemaleTeamsList(List<Map<String,String>> l){
        savedStateHandle.set(femaleTeamsKey, l);
    }

    public MutableLiveData<List<Map<String,String>>> getMaleTeamsList(){
        // Get the LiveData, setting the default value if it doesn't already have a value set.
        return savedStateHandle.getLiveData(maleTeamsKey,  new ArrayList<>());
    }
    public void setMaleTeamsList(List<Map<String,String>> l){
        savedStateHandle.set(maleTeamsKey, l);
    }

    public MutableLiveData<List<VCNews>> getVcNewsList(){
        // Get the LiveData, setting the default value if it doesn't already have a value set.
        return savedStateHandle.getLiveData(newsKey, new ArrayList<>());
    }
    public void setVcNewsList(List<VCNews> l){
        savedStateHandle.set(newsKey, l);
    }

    public MutableLiveData<List<FavoritesWrapper>> getFavoritesList(){
        // Get the LiveData, setting the default value if it doesn't already have a value set.
        return savedStateHandle.getLiveData(favoritesKey, new ArrayList<>());
    }
    public void setFavoritesListList(List<FavoritesWrapper> l){
        savedStateHandle.set(favoritesKey, l);
    }

    /*
    // LiveData is a lifecycle-aware observable data holder class
    // MutableLiveData allows its value to be changed
    private final MutableLiveData< List<Map<String,String>> > teamsList;

    public TeamsViewModel() {
        teamsList = new MutableLiveData<>();
        teamsList.setValue(new ArrayList<>());
    }

    public void setTeamsList(List<Map<String,String>> l){
        teamsList.setValue(l);
    }
    public LiveData<List<Map<String,String>>> getTeamsList() {
        return teamsList;
    }
    */
}