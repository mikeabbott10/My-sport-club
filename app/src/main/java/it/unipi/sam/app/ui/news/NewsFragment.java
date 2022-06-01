package it.unipi.sam.app.ui.news;

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
import it.unipi.sam.app.databinding.FragmentNewsBinding;
import it.unipi.sam.app.util.ItemViewModel;

public class NewsFragment extends Fragment {
    private FragmentNewsBinding binding;

    private ItemViewModel viewModel;
    private ClipData.Item item;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewsViewModel newsViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // nota requireActivity() : same scope as in the activity is required or different ViewModel!
        item = new ClipData.Item(requireActivity().getString(R.string.menu_notizie));
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);


        final TextView textView = binding.textHome;
        newsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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