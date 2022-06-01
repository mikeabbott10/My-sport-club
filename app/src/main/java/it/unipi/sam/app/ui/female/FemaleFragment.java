package it.unipi.sam.app.ui.female;

import android.content.ClipData;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.FragmentFemaleBinding;
import it.unipi.sam.app.util.ItemViewModel;

public class FemaleFragment extends Fragment {

    private FragmentFemaleBinding binding;

    private ItemViewModel viewModel;
    private ClipData.Item item;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FemaleViewModel femaleViewModel =
                new ViewModelProvider(this).get(FemaleViewModel.class);

        binding = FragmentFemaleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // nota requireActivity() : same scope as in the activity is required or different ViewModel!
        item = new ClipData.Item(requireActivity().getString(R.string.menu_femminile));
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        final TextView textView = binding.textGallery;
        femaleViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.selectItem(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}