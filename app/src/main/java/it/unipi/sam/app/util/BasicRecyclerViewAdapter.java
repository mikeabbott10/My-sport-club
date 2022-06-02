package it.unipi.sam.app.util;

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

import java.util.List;

import it.unipi.sam.app.R;

public class BasicRecyclerViewAdapter extends RecyclerView.Adapter<BasicRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView appName;
        TextView appDesc;
        ImageView appImage;

        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            appName = itemView.findViewById(R.id.app_name);
            appDesc = itemView.findViewById(R.id.app_description);
            appImage = itemView.findViewById(R.id.app_image);
        }
    }

    Context context;
    List<VCNews> news;

    public BasicRecyclerViewAdapter(List<VCNews> news, Context ctx){
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
            Toast.makeText(context, "Impossibile lanciare l'activity", Toast.LENGTH_SHORT).show();
            return;
        }
        //(Class<?>) ((ParamLinearLayout) view).getObj() è la classe relativa all'activity da lanciare
        Intent in = new Intent(context, (Class<?>) ((ParamLinearLayout) view).getObj());
        context.startActivity(in);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup vg, int i) {
        View v = LayoutInflater.from(vg.getContext()).inflate(R.layout.item, vg, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder appViewHolder, int i) {
        appViewHolder.appName.setText(apps.get(i).appName);
        appViewHolder.appDesc.setText(apps.get(i).appDescription);
        appViewHolder.appImage.setImageResource(apps.get(i).appImageID);
        if(apps.get(i).classe != null)
            ((ParamLinearLayout) appViewHolder.itemView ).setObject(apps.get(i).classe);
        appViewHolder.itemView.setOnClickListener(this);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
