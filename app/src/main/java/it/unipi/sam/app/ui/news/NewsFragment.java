package it.unipi.sam.app.ui.news;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import it.unipi.sam.app.MainActivity;
import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.FragmentNewsBinding;
import it.unipi.sam.app.util.BasicRecyclerViewAdapter;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.ItemViewModel;

public class NewsFragment extends Fragment {
    private FragmentNewsBinding binding;

    private ItemViewModel viewModel;
    private ClipData.Item item;

    @SuppressLint("NotifyDataSetChanged")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewsViewModel newsViewModel =
                new ViewModelProvider(requireActivity()).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // nota requireActivity() : same scope as in the activity is required or different ViewModel!
        item = new ClipData.Item(requireActivity().getString(R.string.menu_notizie));
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        final RecyclerView recycleView = binding.newsRecyclerView;
        final NestedScrollView nsv = binding.newsContainer;
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(llm);
        recycleView.setHasFixedSize(true);
        final BasicRecyclerViewAdapter adapter = new BasicRecyclerViewAdapter(null, getActivity());
        recycleView.setAdapter(adapter);
        newsViewModel.getVcNewsList().observe(getViewLifecycleOwner(), item ->{
            nsv.setVisibility(View.GONE);
            adapter.setNews(item);
            adapter.notifyDataSetChanged(); // ultima spiaggia
        });
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