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

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

import it.unipi.sam.app.MainActivity;
import it.unipi.sam.app.R;
import it.unipi.sam.app.activities.overview.TeamOverviewActivity;
import it.unipi.sam.app.util.graphics.ParamLinearLayout;

public class TeamsRecyclerViewAdapter extends RecyclerView.Adapter<TeamsRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    public static class ViewHolder extends RecyclerView.ViewHolder {
    CardView cv;
    TextView name;
    ImageView image;

    ViewHolder(View itemView) {
        super(itemView);
        cv = itemView.findViewById(R.id.cv);
        name = itemView.findViewById(R.id.cv_name);
        image = itemView.findViewById(R.id.cv_image);
    }
}

    Context context;
    List<Map<String,String>> teams;
    public void setTeams(List<Map<String,String>> teams) {
        this.teams = teams;
    }
    public TeamsRecyclerViewAdapter(List<Map<String,String>> teams, Context ctx) {
        this.teams = teams;
        context = ctx;
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    @Override
    public void onClick(View view) {
        if( ((ParamLinearLayout) view).getObj() == null){
            Toast.makeText(context, "Retry later.", Toast.LENGTH_SHORT).show();
            return;
        }

        // (String) ((ParamLinearLayout) view).getObj() è il code del team cliccato
        Intent i = new Intent(context, TeamOverviewActivity.class);
        i.putExtra(Constants.teamCode, (String) ((ParamLinearLayout) view).getObj());
        i.putExtra(Constants.rest_info_instance_key, ((MainActivity)context).restInfoInstance);
        context.startActivity(i);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup vg, int i) {
        View v = LayoutInflater.from(vg.getContext()).inflate(R.layout.teams_item, vg, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TeamsRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        viewHolder.name.setText(teams.get(i).get(Constants.resource_name));
        // load image
        if( ((MainActivity)context).restInfoInstance != null)
        Glide
                .with(context)
                .load(Constants.restBasePath + ((MainActivity)context).restInfoInstance.getTeamsPath() + teams.get(i).get(Constants.resource_tag) + "/"
                        + ((MainActivity)context).restInfoInstance.getKeyWords().get(Constants.presentationImage))
                //.centerCrop()
                .placeholder(R.drawable.placeholder_126)
                .error(R.drawable.placeholder_126)
                .into(viewHolder.image);
        ((ParamLinearLayout) viewHolder.itemView ).setObject(teams.get(i).get(Constants.resource_tag));
        viewHolder.itemView.setOnClickListener(this);
        //DebugUtility.LogDThis(DebugUtility.TOUCH_OR_CLICK_RELATED_LOG, "AAAA", "pos: "+i, null);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
