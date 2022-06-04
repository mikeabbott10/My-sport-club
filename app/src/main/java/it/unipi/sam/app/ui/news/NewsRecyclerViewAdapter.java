package it.unipi.sam.app.ui.news;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.unipi.sam.app.MainActivity;
import it.unipi.sam.app.R;
import it.unipi.sam.app.activities.ScreenSlidePagerActivity;
import it.unipi.sam.app.util.ParamLinearLayout;
import it.unipi.sam.app.util.VCNews;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView desc;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            name = itemView.findViewById(R.id.cv_name);
            desc = itemView.findViewById(R.id.cv_description);
            image = itemView.findViewById(R.id.cv_image);
        }
    }

    Context context;
    List<VCNews> news;
    public void setNews(List<VCNews> news) {
        this.news = news;
    }

    public NewsRecyclerViewAdapter(List<VCNews> news, Context ctx){
        this.news = news;
        context = ctx;
        // uncomment this in order to see if the images are from the server/disk/memory.
        // It will show up a flag on the top left corner of your pictures.
        //  - Red flag means the images come from the server. (No caching at first load)
        //  - Blue flag means the photos come from the local disk. (Caching)
        //  - Green flag means the images come from the memory. (Instance Caching)
        // Picasso.get().setIndicatorsEnabled(true);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    @Override
    public void onClick(View view) {
        if( ((ParamLinearLayout) view).getObj() == null){
            Toast.makeText(context, "Retry later.", Toast.LENGTH_SHORT).show();
            return;
        }
        //(VCNews) ((ParamLinearLayout) view).getObj() è la notizia relativa alla view cliccata
        // apri notizia
        Intent i = new Intent(context, ScreenSlidePagerActivity.class);
        i.putExtra(context.getString(R.string.news), (VCNews) ((ParamLinearLayout) view).getObj());
        i.putExtra(context.getString(R.string.rest_info_instance_key), ((MainActivity)context).restInfoInstance);
        context.startActivity(i);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup vg, int i) {
        View v = LayoutInflater.from(vg.getContext()).inflate(R.layout.item, vg, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.name.setText(news.get(i).getTitle());
        viewHolder.desc.setText(news.get(i).getDescription());
        // load vertical image
        Picasso.get().load(context.getString(R.string.restBasePath) + news.get(i).getResourcePath() + "/" + news.get(i).getLogoImgName()).into(viewHolder.image);
        ((ParamLinearLayout) viewHolder.itemView ).setObject(news.get(i));
        viewHolder.itemView.setOnClickListener(this);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
