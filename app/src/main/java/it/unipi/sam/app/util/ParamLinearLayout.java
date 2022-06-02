package it.unipi.sam.app.util;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ParamLinearLayout extends LinearLayout {
    private Object obj;

    public ParamLinearLayout(Context context) {
        super(context);
    }

    public ParamLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParamLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ParamLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setObject(Object object) {
        this.obj = object;
    }
    public Object getObj() {
        return obj;
    }
}