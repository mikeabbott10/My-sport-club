package it.unipi.sam.app.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import it.unipi.sam.app.util.VCNews;

public class NewsViewModel extends ViewModel {

    private final MutableLiveData<List<VCNews>> vcNewsList;

    public NewsViewModel() {
        vcNewsList = new MutableLiveData<>();
        vcNewsList.setValue(new ArrayList<>());
    }

    public void setVcNewsList(List<VCNews> l){
        vcNewsList.setValue(l);
    }
    public LiveData<List<VCNews>> getVcNewsList() {
        return vcNewsList;
    }

}