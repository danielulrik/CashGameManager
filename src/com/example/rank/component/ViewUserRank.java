package com.example.rank.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.rank.Formatador;
import com.example.rank.R;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 17/07/2015
 * Time: 09:38
 */
public class ViewUserRank extends LinearLayout {

    private TextView textViewPositionNumber;
    private TextView textViewUserName;
    private TextView textViewBalanceValue;

    public ViewUserRank(Context context) {
        this(context, null);
    }

    public ViewUserRank(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewUserRank(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_user_pos_rank, this);

        textViewPositionNumber = (TextView) findViewById(R.id.text_view_position_rank);
        textViewUserName = (TextView) findViewById(R.id.text_view_user_name);
        textViewBalanceValue = (TextView) findViewById(R.id.position_balance_value);
    }

    public void setUserValue(double value) {
        if (value > 0) {
            textViewBalanceValue.setTextColor(Color.GREEN);
        } else if (value == 0) {
            textViewBalanceValue.setTextColor(Color.WHITE);
        } else {
            textViewBalanceValue.setTextColor(Color.RED);
        }
        textViewBalanceValue.setText(Formatador.formatarDouble(value));
    }

    public void setUserName(String name) {
        textViewUserName.setText(name);
    }

    public void setPositionNumber(int positionNumber) {
        textViewPositionNumber.setText(positionNumber + getContext().getString(R.string.simbol_position));
        if (positionNumber == 1) {
            textViewUserName.setTypeface(null, Typeface.BOLD);
            textViewBalanceValue.setTypeface(null, Typeface.BOLD);
        }
    }

    public void setRankType(Type rankType) {
        LinearLayout layoutExtraInf = (LinearLayout) findViewById(R.id.layout_extra_information);
//        if (rankType.equals(Type.TYPE_ALL)) {
//            layoutExtraInf.setVisibility(VISIBLE);
//            ((TextView) layoutExtraInf.findViewById(R.id.text_view_numero_vencidos)).setText("X");
//        } else {
//        }
        layoutExtraInf.setVisibility(GONE);
    }

    public enum Type {
        TYPE_ROUND, TYPE_ALL
    }
}
