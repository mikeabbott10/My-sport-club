package it.unipi.sam.app.util;

import android.content.ClipData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {
    // LiveData is a lifecycle-aware observable data holder class
    // MutableLiveData allows its value to be changed
    private final MutableLiveData<ClipData.Item> selectedItem = new MutableLiveData<ClipData.Item>();

    public void selectItem(ClipData.Item item) {
        selectedItem.setValue(item);
    }
    public LiveData<ClipData.Item> getSelectedItem() {
        return selectedItem;
    }
}