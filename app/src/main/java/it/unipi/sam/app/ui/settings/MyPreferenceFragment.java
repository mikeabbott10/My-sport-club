package it.unipi.sam.app.ui.settings;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import it.unipi.sam.app.R;
import it.unipi.sam.app.util.ItemViewModel;

public class MyPreferenceFragment extends PreferenceFragmentCompat {
    private ItemViewModel viewModel;
    private String item;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.my_preferences, rootKey);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            // no need for verified domain settings
            // disable them
            Preference domainVerificationCategory =  getPreferenceScreen().getPreference(0); // nota: 0 è l'indice della prima PreferenceCategory in PreferenceScreen
            domainVerificationCategory.setEnabled(false);
            domainVerificationCategory.setTitle(getString(R.string.domainverification_title) + " (" + getString(R.string.not_supported) + ")");

        }
        item = requireActivity().getString(R.string.menu_settings);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.selectItem(item);
    }
}