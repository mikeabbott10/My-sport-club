package it.unipi.sam.app.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {
    // LiveData is a lifecycle-aware observable data holder class
    // MutableLiveData allows its value to be changed
    private final MutableLiveData<String> selectedItem = new MutableLiveData<>();

    public void selectItem(String item) {
        selectedItem.setValue(item);
    }
    public LiveData<String> getSelectedItem() {
        return selectedItem;
    }
}