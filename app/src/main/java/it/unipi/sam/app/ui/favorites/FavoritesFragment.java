package it.unipi.sam.app.ui.favorites;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import it.unipi.sam.app.R;
import it.unipi.sam.app.databinding.FragmentFavoritesBinding;
import it.unipi.sam.app.util.DebugUtility;
import it.unipi.sam.app.util.FavoritesWrapper;
import it.unipi.sam.app.util.ItemViewModel;
import it.unipi.sam.app.util.room.AppDatabase;

public class FavoritesFragment extends Fragment implements Observer<List<FavoritesWrapper>>, SetFavoritesListener, RetriveFavoritesListener {
    private final String TAG = "FRFRFavoritesFragment";
    private FragmentFavoritesBinding binding;

    private ItemViewModel viewModel;
    private String currentFragmentName;
    private FavoritesRecyclerViewAdapter adapter;

    private AppDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "onCreateView()", null);
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // nota requireActivity() : same scope as in the activity is required or different ViewModel!
        currentFragmentName = requireActivity().getString(R.string.menu_favoriti);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // Nota: ottengo preferiti in MainActivity asincronamente.

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        binding.favoritesRecyclerView.setLayoutManager(llm);
        binding.favoritesRecyclerView.setHasFixedSize(true);
        adapter = new FavoritesRecyclerViewAdapter(new ArrayList<>(), getActivity());
        binding.favoritesRecyclerView.setAdapter(adapter);
        viewModel.getFavoritesList().observe(getViewLifecycleOwner(), this);

        // ROOM
        db = AppDatabase.getDatabase(requireActivity().getApplicationContext());

        // initialize favorites view swipe related objects
        int deleteColor = ContextCompat.getColor(requireActivity(), R.color.trasp_dark_red);
        Drawable deleteIcon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_delete_outline_24);
        assert deleteIcon != null;
        deleteIcon.setTint(deleteColor);
        Bitmap deleteIconBitmap = convertToBitmap(deleteIcon, deleteIcon.getIntrinsicWidth(), deleteIcon.getIntrinsicHeight());
        Paint p = new Paint();
        ItemTouchHelper swipeHelper = new ItemTouchHelper(getItemTouchCallbacks(deleteIconBitmap, p));
        swipeHelper.attachToRecyclerView(binding.favoritesRecyclerView);

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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onChanged(List<FavoritesWrapper> list) {
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "viewModel.getFavoritesList().observe . list:", null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.stream().map(s -> "item: " + s).forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    Log.d(TAG, s);
                }
            });
        }
        if(list!=null) {
            adapter.setmFavorites(list);
            // idk quante entries ci sono in più o in meno rispetto a prima (nè dove sono state inserite/eliminate).
            // E' quindi necessario un refresh dell'intero data set:
            adapter.notifyDataSetChanged();
        }
        if(list==null || list.size()==0){
            Snackbar.make(binding.getRoot(), getString(R.string.no_favorites), 6000).show();
        }

    }


    @Override
    public void onFavoritesSetted(Object obj, int operation) {
        // launch thread to retrive updated favorites from db and
        new Thread(new RetriveFavoritesRunnable(this, db)).start();
    }

    // RetriveFavoritesListener implementation
    @Override
    public void onFavoritesRetrived(List<FavoritesWrapper> favorites) {
        viewModel.setFavoritesList(favorites);
        DebugUtility.LogDThis(DebugUtility.IDENTITY_LOG, TAG, "onFavoritesRetrived()", null);
    }

    /**
     * Ottieni un oggetto ItemTouchHelper.SimpleCallback per la gestione dello swipe
     * sulle view del recycler view.
     * @param deleteIconBitmap
     * @param p
     * @return
     */
    private ItemTouchHelper.SimpleCallback getItemTouchCallbacks(Bitmap deleteIconBitmap, Paint p) {
        return
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        // never called (no drag n drop)
                        return true;
                    }

                    @Override
                    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                        return .6f;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        if(viewModel.getFavoritesList().getValue() == null)
                            return;
                        int pos = viewHolder.getAdapterPosition();
                        switch(viewHolder.getItemViewType()/*fav.getInstance()*/) {
                            case FavoritesWrapper.FAVORITE_PERSON: {
                                int prevPos = pos;
                                pos--;
                                try {
                                    SetFavoritesRunnable r =
                                            new SetFavoritesRunnable(db,
                                                    new FavoritesWrapper(viewModel.getFavoritesList().getValue().get(pos).getPerson()),
                                                    FavoritesFragment.this, prevPos
                                            );
                                    new Thread(r).start();
                                }catch (IllegalArgumentException ignored){}
                                break;
                            }
                            case FavoritesWrapper.FAVORITE_TEAM: {
                                //Log.d(TAG, "pos:"+pos+", insertedSeparators:"+adapter.getInsertedSeparators());
                                int prevPos = pos;
                                if(adapter.getInsertedSeparators()==1){
                                    // no people in list
                                    pos--;
                                }else // persone e teams in list
                                    pos-=2;
                                try {
                                    SetFavoritesRunnable r =
                                            new SetFavoritesRunnable(db,
                                                    new FavoritesWrapper(viewModel.getFavoritesList().getValue().get(pos).getTeam()),
                                                    FavoritesFragment.this, prevPos
                                            );
                                    new Thread(r).start();
                                }catch (IllegalArgumentException ignored){}
                                break;
                            }
                            case FavoritesWrapper.FAVORITE_NEWS: {
                                int prevPos = pos;
                                pos -= adapter.getInsertedSeparators();
                                try {
                                    SetFavoritesRunnable r =
                                            new SetFavoritesRunnable(db,
                                                    new FavoritesWrapper(viewModel.getFavoritesList().getValue().get(pos).getNews()),
                                                    FavoritesFragment.this, prevPos
                                            );
                                    new Thread(r).start();
                                }catch (IllegalArgumentException ignored){}
                                break;
                            }
                        }

                    }

                    @Override
                    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                        if ( ((FavoritesRecyclerViewAdapter.ViewHolder)viewHolder).isSeparator ) return 0;
                        return super.getSwipeDirs(recyclerView, viewHolder);
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 3;

                        RectF icon_dest = new RectF(
                                (float) itemView.getLeft() + 70,
                                (float) itemView.getTop() + width,
                                (float) itemView.getLeft() + 70 + width,
                                (float) itemView.getBottom() - width);
                        c.drawBitmap(deleteIconBitmap, null, icon_dest, p);

                        super.onChildDraw(c, recyclerView, viewHolder, dX/3, dY, actionState, isCurrentlyActive);
                    }
                };
    }

    /**
     * Drawable to Bitmap
     * @param drawable
     * @param widthPixels
     * @param heightPixels
     * @return
     */
    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }
}
