package it.unipi.sam.app.util;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface OnSwipeCallbackListener {
    void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction);
    float onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive);
    int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder);
}
