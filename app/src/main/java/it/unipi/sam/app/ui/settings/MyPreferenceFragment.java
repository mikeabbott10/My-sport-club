package it.unipi.sam.app.ui.settings;

import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

import it.unipi.sam.app.R;
import it.unipi.sam.app.util.ItemViewModel;

public class MyPreferenceFragment extends PreferenceFragmentCompat {
    private ItemViewModel viewModel;
    private ClipData.Item item;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.my_preferences, rootKey);
        item = new ClipData.Item(requireActivity().getString(R.string.menu_settings));
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.selectItem(item);
    }
}