package it.unipi.sam.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.FragmentScreenSlidePageBinding;
import it.unipi.sam.app.util.VCNews;

public class ScreenSlidePageFragment extends Fragment {
    FragmentScreenSlidePageBinding binding;
    VCNews thisNews;

    public ScreenSlidePageFragment(VCNews n) {
        this.thisNews = n;
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        binding = FragmentScreenSlidePageBinding.inflate(inflater, container, false);
        // set date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("it", "IT"));
        String date = simpleDateFormat.format(new Date(thisNews.getDate()));
        binding.fDate.setText(date);
        // set title
        binding.newsTitle.setText(thisNews.getTitle());
        // set author
        String autore = getString(R.string.autore) + thisNews.getAuthor();
        binding.newsAuthor.setText(autore);
        // set content
        binding.newsParagraph.setText(Html.fromHtml(thisNews.getDescription()));
        // load/set image
        Picasso.get().load(requireActivity().getString(R.string.restBasePath) + thisNews.getResourcePath() + "/" + thisNews.getCoverImgName()).into( binding.newsImage );
        return binding.getRoot();
    }
}