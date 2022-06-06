package it.unipi.sam.app.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.ActivityMainBinding;
import it.unipi.sam.app.databinding.ActivityScreenSlideBinding;
import it.unipi.sam.app.ui.ScreenSlidePageFragment;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.VCNews;
import it.unipi.sam.app.util.graphics.DepthPageTransformer;
import it.unipi.sam.app.util.graphics.ZoomOutPageTransformer;

public class ScreenSlidePagerActivity extends AppCompatActivity {
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    ActivityScreenSlideBinding binding;
    List<VCNews> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int news_position = 0;
        long news_id;
        try{
            news = (ArrayList<VCNews>) getIntent().getSerializableExtra(getString(R.string.news));
            news_id = getIntent().getLongExtra(getString(R.string.news_id), -1);
            for (int index = 0; index<news.size(); ++index) {
                if(news.get(index).getId() == news_id) {
                    news_position = index;
                    DebugUtility.LogDThis(DebugUtility.TOUCH_OR_CLICK_RELATED_LOG, "AAAA", news.get(index).getTitle()+"--"+index, null);
                    DebugUtility.LogDThis(DebugUtility.TOUCH_OR_CLICK_RELATED_LOG, "AAAA", "-id-"+news_id, null);
                    break;
                }
            }
        }catch (ClassCastException e){
            e.printStackTrace();
            finish();
            return;
        }
        binding = ActivityScreenSlideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.fToolbar);
        //binding.fToolbar.setTitle("");
        // Enable the Up button
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("");
            binding.fToolbar.setNavigationIcon(R.drawable.ic_back);
        }


        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = binding.pager;
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(news_position, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }*/

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new ScreenSlidePageFragment(news.get(position));
        }

        @Override
        public int getItemCount() {
            return news.size();
        }
    }
}
