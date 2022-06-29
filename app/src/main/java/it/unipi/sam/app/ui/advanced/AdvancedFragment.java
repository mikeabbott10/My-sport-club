package it.unipi.sam.app.ui.advanced;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import it.unipi.sam.app.R;
import it.unipi.sam.app.activities.advanced.TimerFunctionActivity;
import it.unipi.sam.app.activities.advanced.TrackerFunctionActivity;
import it.unipi.sam.app.databinding.FragmentAdvancedBinding;
import it.unipi.sam.app.util.ItemViewModel;

public class AdvancedFragment extends Fragment implements View.OnClickListener {
    private FragmentAdvancedBinding binding;

    private ItemViewModel viewModel;
    private String currentFragmentName;
    //private final String TAG = "FRFRAdvancedFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdvancedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // nota requireActivity() : same scope as in the activity is required or different ViewModel!
        currentFragmentName = requireActivity().getString(R.string.menu_avanzate);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        binding.cronometroLayout.setOnClickListener(this);
        binding.trackerLayout.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.tracker_layout){
            startActivity(new Intent(requireActivity(), TrackerFunctionActivity.class));
        }else if(view.getId()==R.id.cronometro_layout){
            startActivity(new Intent(requireActivity(), TimerFunctionActivity.class));
        }
    }
}