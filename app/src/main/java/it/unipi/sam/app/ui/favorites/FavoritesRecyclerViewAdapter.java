package it.unipi.sam.app.ui.favorites;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
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
import java.util.Collections;
import java.util.List;

import it.unipi.sam.app.MainActivity;
import it.unipi.sam.app.R;
import it.unipi.sam.app.activities.BasicActivity;
import it.unipi.sam.app.activities.ScreenSlidePagerActivity;
import it.unipi.sam.app.activities.overview.PeopleOverviewActivity;
import it.unipi.sam.app.activities.overview.TeamOverviewActivity;
import it.unipi.sam.app.util.Constants;
import it.unipi.sam.app.util.FavoritesWrapper;
import it.unipi.sam.app.util.Person;
import it.unipi.sam.app.util.Team;
import it.unipi.sam.app.util.VCNews;
import it.unipi.sam.app.util.graphics.ParamLinearLayout;

public class FavoritesRecyclerViewAdapter extends RecyclerView.Adapter<FavoritesRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private static final int SEPARATOR = 99;

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
    List<Object> mFavorites;
    private String personSeparator, teamSeparator, newsSeparator;

    public void setmFavorites(List<FavoritesWrapper> favorites) {
        Collections.sort(favorites);
        this.mFavorites = new ArrayList<>((List<Object>) (List) favorites);
        if(favorites.size()==0)
            return;
        int timesToSubstract = 0;
        // nota: non aggiunge il primo separatore (posizione 0)
        for(int i = 1; i< favorites.size(); ++i){
            int currentType = favorites.get(i).getInstance(); // can be FavoritesWrapper.FAVORITE_PERSON, FavoritesWrapper.FAVORITE_TEAM, FavoritesWrapper.FAVORITE_NEWS
            int prevFavType = favorites.get(i-1).getInstance();
            if(prevFavType==FavoritesWrapper.FAVORITE_NEWS)
                break;
            if(prevFavType != currentType){
                switch(currentType){
                    case FavoritesWrapper.FAVORITE_PERSON:{
                        mFavorites.add(i-timesToSubstract++, personSeparator);
                        break;
                    }
                    case FavoritesWrapper.FAVORITE_TEAM:{
                        mFavorites.add(i-timesToSubstract++, teamSeparator);
                        break;

                    }
                    case FavoritesWrapper.FAVORITE_NEWS:{
                        mFavorites.add(i-timesToSubstract++, newsSeparator);
                        break;

                    }
                }
            }
        }
        // ora aggiungo il primo separatore (posizione 0)
        switch(favorites.get(0).getInstance()){
            case FavoritesWrapper.FAVORITE_PERSON:{
                mFavorites.add(0, personSeparator);
                break;
            }
            case FavoritesWrapper.FAVORITE_TEAM:{
                mFavorites.add(0, teamSeparator);
                break;

            }
            case FavoritesWrapper.FAVORITE_NEWS:{
                mFavorites.add(0, newsSeparator);
                break;

            }
        }
    }

    public FavoritesRecyclerViewAdapter(List<FavoritesWrapper> favorites, Context ctx){
        this.mFavorites = new ArrayList<>((List<Object>) (List) favorites);
        context = ctx;

        personSeparator = context.getString(R.string.people);
        teamSeparator = context.getString(R.string.team);
        newsSeparator = context.getString(R.string.news);
    }

    @Override
    public int getItemViewType(int i) {
        Object obj = mFavorites.get(i);
        if(obj instanceof String)
            return SEPARATOR;
        return ((FavoritesWrapper)obj).getInstance();
    }

    @Override
    public void onClick(View view) {
        if( ((ParamLinearLayout) view).getObj() == null){
            Toast.makeText(context, "Retry later.", Toast.LENGTH_SHORT).show();
            return;
        }
        // (int) ((Object[])((ParamLinearLayout) view).getObj())[0] è FavoritesWrapper.FAVORITE_NEWS || FavoritesWrapper.FAVORITE_TEAM || FavoritesWrapper.FAVORITE_PERSON
        int category = (int) ((Object[])((ParamLinearLayout) view).getObj())[0];
        switch(category) {
            case FavoritesWrapper.FAVORITE_NEWS: {
                // (VCNews) ((Object[])((ParamLinearLayout) view).getObj())[1] è l'istanza della news
                VCNews news = (VCNews) ((Object[])((ParamLinearLayout) view).getObj())[1];
                // apri notizia
                Intent i = new Intent(context, ScreenSlidePagerActivity.class);
                try{
                    ArrayList<VCNews> singleton = new ArrayList<>();
                    singleton.add(news);
                    i.putExtra(Constants.news_key, singleton);
                }catch (ClassCastException e){
                    e.printStackTrace();
                    Toast.makeText(context, "ERROR 02. Retry later.", Toast.LENGTH_SHORT).show();
                    return;
                }
                i.putExtra(Constants.news_id_key, news.getId());
                i.putExtra(Constants.rest_info_instance_key, ((MainActivity)context).restInfoInstance);
                context.startActivity(i);
                break;
            }
            case FavoritesWrapper.FAVORITE_TEAM: {
                // (String) ((Object[])((ParamLinearLayout) view).getObj())[1] è il codice della squadra
                String teamCode = (String) ((Object[])((ParamLinearLayout) view).getObj())[1];
                Intent i = new Intent(context, TeamOverviewActivity.class);
                i.putExtra(Constants.teamCode, teamCode);
                i.putExtra(Constants.rest_info_instance_key, ((MainActivity)context).restInfoInstance);
                context.startActivity(i);
                break;
            }
            case FavoritesWrapper.FAVORITE_PERSON: {
                // (String) ((Object[])((ParamLinearLayout) view).getObj())[1] è il codice della persona
                String personCode = (String) ((Object[])((ParamLinearLayout) view).getObj())[1];
                Intent i = new Intent(context, PeopleOverviewActivity.class);
                i.putExtra(Constants.peopleCode, personCode);
                i.putExtra(Constants.rest_info_instance_key, ((MainActivity)context).restInfoInstance);
                context.startActivity(i);
                break;
            }
        }
    }

    @NonNull @Override
    public FavoritesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup vg, int viewType) {
        switch(viewType) {
            case SEPARATOR:{
                View v = LayoutInflater.from(vg.getContext()).inflate(R.layout.separator_item, vg, false);
                return new FavoritesRecyclerViewAdapter.ViewHolder(v);
            }
            case FavoritesWrapper.FAVORITE_PERSON: {
                View v = LayoutInflater.from(vg.getContext()).inflate(R.layout.person_item, vg, false);
                return new FavoritesRecyclerViewAdapter.ViewHolder(v);
            }
            case FavoritesWrapper.FAVORITE_TEAM: {
                View v = LayoutInflater.from(vg.getContext()).inflate(R.layout.teams_item, vg, false);
                return new FavoritesRecyclerViewAdapter.ViewHolder(v);
            }
            case FavoritesWrapper.FAVORITE_NEWS:{
                View v = LayoutInflater.from(vg.getContext()).inflate(R.layout.news_item, vg, false);
                return new FavoritesRecyclerViewAdapter.ViewHolder(v);
            }
        }
        assert false;
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        Object obj = mFavorites.get(i);
        if(obj instanceof String) {
            viewHolder.name.setText((String) obj);
            return;
        }
        FavoritesWrapper fav = (FavoritesWrapper) mFavorites.get(i);
        if(fav==null) return;

        switch(viewHolder.getItemViewType()/*fav.getInstance()*/){
            case FavoritesWrapper.FAVORITE_NEWS:
                viewHolder.name.setText(fav.getNews().getTitle());
                viewHolder.desc.setText(Html.fromHtml(fav.getNews().getDescription()));
                // load image
                Glide
                        .with(context)
                        .load(Constants.restBasePath + fav.getNews().getResourcePath() + "/" + fav.getNews().getLogoImgName())
                        //.centerCrop()
                        .placeholder(R.drawable.placeholder_126)
                        .error(R.drawable.placeholder_126)
                        .into(viewHolder.image);
                ((ParamLinearLayout) viewHolder.itemView ).setObject(new Object[]{FavoritesWrapper.FAVORITE_NEWS, fav.getNews()});
                break;
            case FavoritesWrapper.FAVORITE_TEAM: {
                Log.d("TAGGHE", fav.getTeam().toString());
                Team team = fav.getTeam();
                String teamCode = team.getTag();
                viewHolder.name.setText(team.getCurrentLeague());
                viewHolder.desc.setText(Html.fromHtml(team.getLeagueDescription()));
                // load image
                String partialPath = ((MainActivity)context).restInfoInstance.getTeamsPath() + teamCode;
                Glide
                        .with(context)
                        .load(
                                ((BasicActivity)context).getPresentationImagePath(
                                        partialPath,
                                        ((MainActivity)context).restInfoInstance.getLastModified().get(partialPath))
                        )
                        //.centerCrop()
                        .placeholder(R.drawable.placeholder_126)
                        .error(R.drawable.placeholder_126)
                        .into(viewHolder.image);
                ((ParamLinearLayout) viewHolder.itemView ).setObject(new Object[]{FavoritesWrapper.FAVORITE_TEAM, teamCode});
                break;
            }
            case FavoritesWrapper.FAVORITE_PERSON: {
                Person person = fav.getPerson();
                String personCode = person.getTag();
                viewHolder.name.setText(person.getName());
                viewHolder.desc.setText(Html.fromHtml(person.getRole()));
                // load image
                String partialPath = ((MainActivity)context).restInfoInstance.getPeoplePath() + personCode;
                Glide
                    .with(context)
                    .load(
                            ((BasicActivity)context).getProfileImagePath(
                                    partialPath,
                                    ((MainActivity)context).restInfoInstance.getLastModified().get(partialPath))
                    )
                    //.centerCrop()
                    .placeholder(R.drawable.placeholder_126)
                    .error(R.drawable.placeholder_126)
                    .into(viewHolder.image);
                ((ParamLinearLayout) viewHolder.itemView ).setObject(new Object[]{FavoritesWrapper.FAVORITE_PERSON, personCode});
                break;
            }
        }

        viewHolder.itemView.setOnClickListener(this);
        //DebugUtility.LogDThis(DebugUtility.TOUCH_OR_CLICK_RELATED_LOG, "AAAA", "pos: "+i, null);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mFavorites.size();
    }

}
