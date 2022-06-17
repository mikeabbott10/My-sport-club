package it.unipi.sam.app.ui.news;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.List;

import it.unipi.sam.app.util.VCNews;

public class NewsViewModel extends ViewModel {
    private final SavedStateHandle savedStateHandle;
    private static final String newsKey = "nk";


    public NewsViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    MutableLiveData<List<VCNews>> getVcNewsList(){
        // Get the LiveData, setting the default value if it doesn't already have a value set.
        return savedStateHandle.getLiveData(newsKey, null);
    }

    public void setVcNewsList(List<VCNews> l){
        savedStateHandle.set(newsKey, l);
    }

    /*
    private final MutableLiveData<List<VCNews>> vcNewsList;

    public NewsViewModel() {
        vcNewsList = new MutableLiveData<>();
        vcNewsList.setValue(new ArrayList<>());
    }

    public LiveData<List<VCNews>> getVcNewsList() {
        return vcNewsList;
    }
    public void setVcNewsList(List<VCNews> l) {
        vcNewsList.setValue(l);
    }
    */
}