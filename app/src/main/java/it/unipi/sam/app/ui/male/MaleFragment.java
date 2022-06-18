package it.unipi.sam.app.ui.male;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.FragmentTeamBinding;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.ItemViewModel;
import it.unipi.sam.app.util.TeamsRecyclerViewAdapter;

public class MaleFragment extends Fragment implements Observer< List<Map<String, String>> > {
    private FragmentTeamBinding binding;

    private ItemViewModel viewModel;
    private String item;
    private String TAG = "FRFRMaleFragment";
    private MaleTeamsViewModel maleViewModel;
    private TeamsRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        maleViewModel =
                new ViewModelProvider(requireActivity()).get(MaleTeamsViewModel.class);

        binding = FragmentTeamBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // nota requireActivity() : same scope as in the activity is required or different ViewModel!
        item = requireActivity().getString(R.string.menu_maschile);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        final RecyclerView recycleView = binding.teamsRecyclerView;
        final NestedScrollView nsv = binding.teamsPlaceholder;
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(llm);
        recycleView.setHasFixedSize(true);
        adapter = new TeamsRecyclerViewAdapter(new ArrayList<>(), getActivity());
        recycleView.setAdapter(adapter);
        maleViewModel.getTeamsList().observe(getViewLifecycleOwner(), this);
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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onChanged(List<Map<String, String>> list) {
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "maleViewModel.getTeamsList().observe . list:"+ list, null);
        if(list!=null) {
            binding.teamsPlaceholder.setVisibility(View.GONE);
            adapter.setTeams(list);
            // idk quante entries ci sono in più o in meno rispetto a prima (nè dove sono state inserite/eliminate).
            // E' quindi necessario un refresh dell'intero data set:
            adapter.notifyDataSetChanged();
        }
    }
}