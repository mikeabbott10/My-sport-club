package it.unipi.sam.app.ui.news;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.unipi.sam.app.MainActivity;
import it.unipi.sam.app.R;
import it.unipi.sam.app.activities.ScreenSlidePagerActivity;
import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.graphics.ParamLinearLayout;
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
        // (ArrayList<VCNews>) ((Object[])((ParamLinearLayout) view).getObj())[0] è la lista di news
        // (int) ((Object[])((ParamLinearLayout) view).getObj())[1] è l'id della news cliccata
        // apri notizia
        Intent i = new Intent(context, ScreenSlidePagerActivity.class);
        try{
            i.putExtra(Constants.news_key, (ArrayList<VCNews>) ((Object[])((ParamLinearLayout) view).getObj())[0]);
        }catch (ClassCastException e){
            e.printStackTrace();
            Toast.makeText(context, "ERROR 02. Retry later.", Toast.LENGTH_SHORT).show();
            return;
        }
        i.putExtra(Constants.news_id_key, (long) ((Object[])((ParamLinearLayout) view).getObj())[1]);
        i.putExtra(Constants.rest_info_instance_key, ((MainActivity)context).restInfoInstance);
        context.startActivity(i);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup vg, int i) {
        View v = LayoutInflater.from(vg.getContext()).inflate(R.layout.news_item, vg, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.name.setText(news.get(i).getTitle());
        viewHolder.desc.setText(Html.fromHtml(news.get(i).getDescription()));
        // load image
        Glide
            .with(context)
            .load(Constants.restBasePath + news.get(i).getResourcePath() + "/" + news.get(i).getLogoImgName())
            //.centerCrop()
            .placeholder(R.drawable.placeholder_126)
            .error(R.drawable.placeholder_126)
            .into(viewHolder.image);
        ((ParamLinearLayout) viewHolder.itemView ).setObject(new Object[]{news, news.get(i).getId()});
        viewHolder.itemView.setOnClickListener(this);
        //DebugUtility.LogDThis(DebugUtility.TOUCH_OR_CLICK_RELATED_LOG, "AAAA", "pos: "+i, null);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
