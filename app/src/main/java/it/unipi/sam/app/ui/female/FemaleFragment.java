package it.unipi.sam.app.ui.female;

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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.FragmentTeamBinding;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.ItemViewModel;
import it.unipi.sam.app.util.TeamsRecyclerViewAdapter;

public class FemaleFragment extends Fragment implements Observer< List<Map<String, String>> > {

    private FragmentTeamBinding binding;

    private ItemViewModel viewModel;
    private String currentFragmentName;
    private String TAG = "FRFRFemaleFragment";
    private TeamsRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTeamBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // nota requireActivity() : same scope as in the activity is required or different ViewModel!
        currentFragmentName = requireActivity().getString(R.string.menu_femminile);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        final RecyclerView recycleView = binding.teamsRecyclerView;
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(llm);
        recycleView.setHasFixedSize(true);
        adapter = new TeamsRecyclerViewAdapter(new ArrayList<>(), getActivity());
        recycleView.setAdapter(adapter);
        viewModel.getFemaleTeamsList().observe(getViewLifecycleOwner(), this);
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
    public void onChanged(List<Map<String, String>> list) {
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "femaleViewModel.getTeamsList().observe . list:"+ list, null);
        if(list!=null) {
            //binding.teamsPlaceholder.setVisibility(View.GONE);
            adapter.setTeams(list);
            // idk quante entries ci sono in più o in meno rispetto a prima (nè dove sono state inserite/eliminate).
            // E' quindi necessario un refresh dell'intero data set:
            adapter.notifyDataSetChanged();
        }
    }
}