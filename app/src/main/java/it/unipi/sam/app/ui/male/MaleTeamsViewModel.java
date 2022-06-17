package it.unipi.sam.app.ui.male;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MaleTeamsViewModel extends ViewModel {
    private final SavedStateHandle savedStateHandle;
    private static final String maleTeamsKey = "tk";

    public MaleTeamsViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    public MutableLiveData<List<Map<String,String>>> getTeamsList(){
        // Get the LiveData, setting the default value if it doesn't already have a value set.
        return savedStateHandle.getLiveData(maleTeamsKey,  new ArrayList<>());
    }

    public void setTeamsList(List<Map<String,String>> l){
        savedStateHandle.set(maleTeamsKey, l);
    }

    /*
    private final MutableLiveData<List<Map<String,String>>> teamsList;

    public MaleTeamsViewModel() {
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