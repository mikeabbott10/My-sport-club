package it.unipi.sam.app.util.graphics;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import it.unipi.sam.app.util.OnSwipeCallbackListener;

public class MyItemTouchHelperSCImpl extends ItemTouchHelper.SimpleCallback {

    private final OnSwipeCallbackListener oscl;

    public MyItemTouchHelperSCImpl(OnSwipeCallbackListener oscl, int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
        this.oscl = oscl;
    }

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
        oscl.onSwiped(viewHolder, direction);
    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if(oscl.getSwipeDirs(recyclerView,viewHolder)==0)
            return 0;
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        float _dX = oscl.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
        super.onChildDraw(c, recyclerView, viewHolder, _dX, dY, actionState, isCurrentlyActive);
    }
}
