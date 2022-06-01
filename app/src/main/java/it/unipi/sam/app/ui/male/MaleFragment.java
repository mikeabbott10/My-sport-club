package it.unipi.sam.app.ui.male;

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
import it.unipi.sam.app.databinding.FragmentMaleBinding;
import it.unipi.sam.app.util.ItemViewModel;

public class MaleFragment extends Fragment {

    private FragmentMaleBinding binding;

    private ItemViewModel viewModel;
    private ClipData.Item item;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MaleViewModel maleViewModel =
                new ViewModelProvider(this).get(MaleViewModel.class);

        binding = FragmentMaleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // nota requireActivity() : same scope as in the activity is required or different ViewModel!
        item = new ClipData.Item(requireActivity().getString(R.string.menu_maschile));
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        final TextView textView = binding.textSlideshow;
        maleViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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