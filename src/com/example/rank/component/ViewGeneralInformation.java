package com.example.rank.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.example.rank.R;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 23/07/2015
 * Time: 09:38
 */
public class ViewGeneralInformation extends LinearLayout {

    public ViewGeneralInformation(Context context) {
        this(context, null);
    }

    public ViewGeneralInformation(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewGeneralInformation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_general_information, null);
    }

}
