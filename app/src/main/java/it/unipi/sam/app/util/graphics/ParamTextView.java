package it.unipi.sam.app.util.graphics;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class ParamTextView extends androidx.appcompat.widget.AppCompatTextView {
    private Object obj;

    public ParamTextView(Context context) {
        super(context);
    }

    public ParamTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParamTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setObject(Object object) {
        this.obj = object;
    }
    public Object getObj() {
        return obj;
    }
}