package it.unipi.sam.app.ui.favorites;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.FragmentFavoritesBinding;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.FavoritesWrapper;
import it.unipi.sam.app.util.ItemViewModel;

public class FavoritesFragment extends Fragment implements Observer<List<FavoritesWrapper>> {
    private final String TAG = "FRFRFavoritesFragment";
    private FragmentFavoritesBinding binding;

    private ItemViewModel viewModel;
    private String currentFragmentName;
    private FavoritesRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // nota requireActivity() : same scope as in the activity is required or different ViewModel!
        currentFragmentName = requireActivity().getString(R.string.menu_favoriti);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // Nota: ottengo preferiti in MainActivity asincronamente.

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        binding.favoritesRecyclerView.setLayoutManager(llm);
        binding.favoritesRecyclerView.setHasFixedSize(true);
        adapter = new FavoritesRecyclerViewAdapter(new ArrayList<>(), getActivity());
        binding.favoritesRecyclerView.setAdapter(adapter);
        viewModel.getFavoritesList().observe(getViewLifecycleOwner(), this);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.selectFragmentName(currentFragmentName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onChanged(List<FavoritesWrapper> list) {
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "viewModel.getFavoritesList().observe . list:"+ list, null);
        if(list!=null) {
            adapter.setFavorites(list);
            // idk quante entries ci sono in più o in meno rispetto a prima (nè dove sono state inserite/eliminate).
            // E' quindi necessario un refresh dell'intero data set:
            adapter.notifyDataSetChanged();
        }
    }
}
