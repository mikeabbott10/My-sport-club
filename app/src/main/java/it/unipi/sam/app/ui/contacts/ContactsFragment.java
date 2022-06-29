package it.unipi.sam.app.ui.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import it.unipi.sam.app.MainActivity;
import it.unipi.sam.app.R;
import it.unipi.sam.app.activities.MapActivity;
import it.unipi.sam.app.databinding.FragmentContactsBinding;
import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.ItemViewModel;
import it.unipi.sam.app.util.SharedPreferenceUtility;

public class ContactsFragment extends Fragment implements View.OnClickListener {

    private FragmentContactsBinding binding;

    private ItemViewModel viewModel;
    private String currentFragmentName;
    private int currentContactsEntry;

    private static final int GOOGLE_MAPS = 0;
    private static final int PHONE = 1;
    private static final int EMAIL = 2;
    private static final int WEBSITE = 3;
    private static final int INSTAGRAM = 4;
    private static final int FACEBOOK = 5;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContactsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // nota requireActivity() : same scope as in the activity is required or different ViewModel!
        currentFragmentName = requireActivity().getString(R.string.menu_contatti);
        currentContactsEntry = -1;
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        binding.addressLayout.setOnClickListener(this);
        binding.phoneLayout.setOnClickListener(this);
        binding.emailLayout.setOnClickListener(this);
        binding.websiteLayout.setOnClickListener(this);
        binding.instagramLayout.setOnClickListener(this);
        binding.facebookLayout.setOnClickListener(this);

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
        binding.img.setAlpha((float) 0.3);
        binding.img1.setAlpha((float) 0.3);
        binding.img2.setAlpha((float) 0.3);
        binding.img3.setAlpha((float) 0.3);
        binding.img4.setAlpha((float) 0.3);
        binding.img5.setAlpha((float) 0.3);

        if(view.getId() == binding.addressLayout.getId()){
            binding.img.setAlpha((float) 1.0);
            if(currentContactsEntry==GOOGLE_MAPS){
                Intent i = new Intent(requireActivity(), MapActivity.class);
                i.putExtra(Constants.lat_lon_marker_key, new LatLng(43.308197250876226, 10.522802259738558));
                i.putExtra(Constants.rest_info_instance_key, ((MainActivity)requireActivity()).restInfoInstance);
                startActivity(i);
                /*startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.sede_maps_link))));*/
            }else{
                currentContactsEntry = GOOGLE_MAPS;
                binding.maintv.startAnimation(AnimationUtils.loadAnimation(requireActivity(),
                        R.anim.shake));
                // se posso ancora mostrare la snackbar: mostra snackbar
                if( ! SharedPreferenceUtility.getDontShowContactsPopup(requireActivity())){
                    Snackbar sb = Snackbar.make(binding.getRoot(), getString(R.string.tap_again_to_maps), 2000);
                    sb.setAction(getString(R.string.dontshowagain), this);
                    sb.show();
                }
            }
        }else if(view.getId() == binding.phoneLayout.getId()){
            binding.img1.setAlpha((float) 1.0);
            if(currentContactsEntry==PHONE){
                startActivity(new Intent(Intent.ACTION_DIAL,
                        Uri.parse(getString(R.string.phone_call_link))));
            }else{
                currentContactsEntry = PHONE;
                binding.maintv1.startAnimation(AnimationUtils.loadAnimation(requireActivity(),
                        R.anim.shake));
                // se posso ancora mostrare la snackbar: mostra snackbar
                if( ! SharedPreferenceUtility.getDontShowContactsPopup(requireActivity())){
                    Snackbar sb = Snackbar.make(binding.getRoot(), getString(R.string.tap_again_to_phone), 2000);
                    sb.setAction(getString(R.string.dontshowagain), this);
                    sb.show();
                }
            }
        }else if(view.getId() == binding.emailLayout.getId()){
            binding.img2.setAlpha((float) 1.0);
            if(currentContactsEntry==EMAIL){
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.mailto_link))));
            }else{
                currentContactsEntry = EMAIL;
                binding.maintv2.startAnimation(AnimationUtils.loadAnimation(requireActivity(),
                        R.anim.shake));
                // se posso ancora mostrare la snackbar: mostra snackbar
                if( ! SharedPreferenceUtility.getDontShowContactsPopup(requireActivity())){
                    Snackbar sb = Snackbar.make(binding.getRoot(), getString(R.string.tap_again_to_email), 2000);
                    sb.setAction(getString(R.string.dontshowagain), this);
                    sb.show();
                }
            }
        }else if(view.getId() == binding.websiteLayout.getId()){
            binding.img3.setAlpha((float) 1.0);
            if(currentContactsEntry==WEBSITE){
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.website_url))));
            }else{
                currentContactsEntry = WEBSITE;
                binding.maintv3.startAnimation(AnimationUtils.loadAnimation(requireActivity(),
                        R.anim.shake));
                // se posso ancora mostrare la snackbar: mostra snackbar
                if( ! SharedPreferenceUtility.getDontShowContactsPopup(requireActivity())){
                    Snackbar sb = Snackbar.make(binding.getRoot(), getString(R.string.tap_again_to_website), 2000);
                    sb.setAction(getString(R.string.dontshowagain), this);
                    sb.show();
                }
            }
        }else if(view.getId() == binding.instagramLayout.getId()){
            binding.img4.setAlpha((float) 1.0);
            if(currentContactsEntry==INSTAGRAM){
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.instagram_link))));
            }else{
                currentContactsEntry = INSTAGRAM;
                binding.maintv4.startAnimation(AnimationUtils.loadAnimation(requireActivity(),
                        R.anim.shake));
                // se posso ancora mostrare la snackbar: mostra snackbar
                if( ! SharedPreferenceUtility.getDontShowContactsPopup(requireActivity())){
                    Snackbar sb = Snackbar.make(binding.getRoot(), getString(R.string.tap_again_to_instagram), 2000);
                    sb.setAction(getString(R.string.dontshowagain), this);
                    sb.show();
                }
            }
        }else if(view.getId() == binding.facebookLayout.getId()){
            binding.img5.setAlpha((float) 1.0);
            if(currentContactsEntry==FACEBOOK){
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.facebook_link))));
            }else{
                currentContactsEntry = FACEBOOK;
                binding.maintv5.startAnimation(AnimationUtils.loadAnimation(requireActivity(),
                        R.anim.shake));
                // se posso ancora mostrare la snackbar: mostra snackbar
                if( ! SharedPreferenceUtility.getDontShowContactsPopup(requireActivity())){
                    Snackbar sb = Snackbar.make(binding.getRoot(), getString(R.string.tap_again_to_facebook), 2000);
                    sb.setAction(getString(R.string.dontshowagain), this);
                    sb.show();
                }
            }
        }else{
            // è stato premuto "Non mostrare più" sulla snackbar
            SharedPreferenceUtility.setDontShowContactsPopup(requireActivity(), true);
        }
    }
}