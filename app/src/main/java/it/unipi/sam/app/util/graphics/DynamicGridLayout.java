package it.unipi.sam.app.util.graphics;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import it.unipi.sam.app.util.DebugUtility;

public class DynamicGridLayout extends ViewGroup {
    public DynamicGridLayout(Context context) {
        super(context);
    }
    public DynamicGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public DynamicGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * dividiamo il nostro spazio in un quadrato di celle di uguale dimensione, e
     * disponiamo i figli chiamando su ciascuno il metodo layout() con le coordinate calcolate.
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int numeroCelleSuUnaRiga = getLato();
        int w = (right-left)/numeroCelleSuUnaRiga; // width di una cella
        int h = (bottom-top)/numeroCelleSuUnaRiga; // height di una cella
        for (int i = 0; i < this.getChildCount(); i++) {
            View v = getChildAt(i);
            int x = i%numeroCelleSuUnaRiga; // # di colonna della cella
            int y = i/numeroCelleSuUnaRiga; // # di riga della cella
            v.layout(x*w, y*h, (x+1)*w, (y+1)*h);
        }
    }
    private int getLato() {
        int layoutWidth = getMeasuredWidth();

        return 3;
    }

    /**
     * Nel nostro esempio, informiamo i figli che vogliamo
     * che ciascuno di loro sia esattamente alto e largo quanto la cella che lo conterrà
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int numeroCelleSuUnaRiga = getLato();
        // getMeasuredWidth() è la width del layout
        // getMeasuredHeight() è la height del layout
        int w = getMeasuredWidth()/numeroCelleSuUnaRiga;
        int h = getMeasuredHeight()/numeroCelleSuUnaRiga;
        DebugUtility.LogDThis(DebugUtility.UI_RELATED_LOG, "CLCL", "w="+w + " - h="+h, null);
        int ws = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY); // width dei figli esattamente w
        int hs = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY); // height dei figli esattamente h
        for(int i = 0; i < this.getChildCount(); i++){
            View v = getChildAt(i);
            v.measure(ws,hs); // il figlio v avrà width ws e height hs
        }
    }
}

