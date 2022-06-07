package it.unipi.sam.app.util.graphics;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ParamImageView extends androidx.appcompat.widget.AppCompatImageView {
    private Object obj;

    public ParamImageView(Context context) {
        super(context);
    }

    public ParamImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParamImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setObject(Object object) {
        this.obj = object;
    }
    public Object getObj() {
        return obj;
    }
}