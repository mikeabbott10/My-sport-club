package it.unipi.sam.app.ui.female;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FemaleTeamsViewModel extends ViewModel {
    private final SavedStateHandle savedStateHandle;
    private static final String femaleTeamsKey = "tk";

    public FemaleTeamsViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    public MutableLiveData<List<Map<String,String>>> getTeamsList(){
        // Get the LiveData, setting the default value if it doesn't already have a value set.
        return savedStateHandle.getLiveData(femaleTeamsKey, new ArrayList<>());
    }

    public void setTeamsList(List<Map<String,String>> l){
        savedStateHandle.set(femaleTeamsKey, l);
    }

    /*
    private final MutableLiveData<List<Map<String,String>>> teamsList;

    public FemaleTeamsViewModel() {
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