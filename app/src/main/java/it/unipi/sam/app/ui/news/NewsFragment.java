package it.unipi.sam.app.ui.news;

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

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.FragmentNewsBinding;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.ItemViewModel;
import it.unipi.sam.app.util.VCNews;

public class NewsFragment extends Fragment implements Observer<List<VCNews>> {
    private String TAG = "FRFRNewsFragment";
    private FragmentNewsBinding binding;

    private ItemViewModel viewModel;
    private String currentFragmentName;
    private NewsRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // nota requireActivity() : same scope as in the activity is required or different ViewModel!
        currentFragmentName = requireActivity().getString(R.string.menu_notizie);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        final RecyclerView recycleView = binding.newsRecyclerView;
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(llm);
        recycleView.setHasFixedSize(true);
        adapter = new NewsRecyclerViewAdapter(new ArrayList<>(), getActivity());
        recycleView.setAdapter(adapter);
        viewModel.getVcNewsList().observe(getViewLifecycleOwner(), this);
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
    public void onChanged(List<VCNews> list) {
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "newsViewModel.getNews().observe . list:"+ list, null);
        if(list!=null) {
            adapter.setNews(list);
            // idk quante entries ci sono in più o in meno rispetto a prima (nè dove sono state inserite/eliminate).
            // E' quindi necessario un refresh dell'intero data set:
            adapter.notifyDataSetChanged();
        }
    }
}