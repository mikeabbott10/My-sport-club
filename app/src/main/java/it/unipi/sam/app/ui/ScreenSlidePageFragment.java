package it.unipi.sam.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.unipi.sam.app.R;
import it.unipi.sam.app.activities.ShareValues;
import it.unipi.sam.app.databinding.FragmentScreenSlidePageBinding;
import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.VCNews;

public class ScreenSlidePageFragment extends Fragment implements View.OnClickListener {
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
        if(!thisNews.getAuthor().equals("")) {
            String autore = getString(R.string.autore) + thisNews.getAuthor();
            binding.newsAuthor.setText(autore);
        }
        // set content
        binding.newsParagraph.setText(Html.fromHtml(thisNews.getDescription()));
        // set share button link
        binding.shareBtn.setOnClickListener(this);
        binding.shareBtn.setObject(thisNews.getId());
        // load/set image
        Glide
                .with(requireActivity())
                .load(Constants.restBasePath + thisNews.getResourcePath() + "/" + thisNews.getCoverImgName())
                //.centerCrop()
                .placeholder(R.drawable.placeholder_126)
                .error(R.drawable.placeholder_126)
                .into(binding.newsImage);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        if(view.equals(binding.shareBtn)){
            startShareNews((long) binding.shareBtn.getObj());
        }
    }

    private void startShareNews(long news_id) {
        Intent i = new Intent(requireActivity(), ShareValues.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Constants.purpose_key, ShareValues.SHARE_NEWS_PURPOSE);
        i.putExtra(Constants.news_id_key, news_id);
        startActivity(i);
    }
}