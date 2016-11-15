package tech.bloomgenetics.bloomapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mdric on 11/15/2016.
 */

public class CrossListObject extends View {
    public CrossListObject(Context context) {
        super(context);
    }
    public CrossListObject(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CrossListObject(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setProjectName(String val){
        TextView mProjectName = (TextView) this.findViewById(R.id.project_name);
        if (mProjectName == null && val != null) {
            mProjectName.setText(val);
        }

    }
}
