package com.example.rank.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.rank.Formatador;
import com.example.rank.R;
import com.example.rank.model.Usuario;
import com.example.rank.screens.AddNewBalanceValue;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 14/07/2015
 * Time: 09:38
 */
public class ViewUserDayBalance extends LinearLayout {

    private Usuario user;
    private TextView textViewUser;
    private TextView textViewUserIn;
    private TextView textViewUserOut;

    public ViewUserDayBalance(Context context) {
        this(context, null);
    }

    public ViewUserDayBalance(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewUserDayBalance(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.view_user_day_balance, this);

        textViewUser = (TextView) findViewById(R.id.text_view_usuario);
        textViewUserIn = (TextView) findViewById(R.id.edit_text_user);
        textViewUserOut = (TextView) findViewById(R.id.edit_text_user_out);
        textViewUserIn.setText(getContext().getString(R.string.initial_value_in));
        configColors();
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
        textViewUser.setText(user.getNome());
    }

    public void setListenerIn(OnClickListener listenerIn) {
        textViewUserIn.setOnClickListener(listenerIn);
    }

    public void setListenerOut(OnClickListener listenerOut) {
        textViewUserOut.setOnClickListener(listenerOut);
    }

    public void setInValue(double value) {
        String formattedValue = Formatador.formatarDouble(value);
        textViewUserIn.setText(formattedValue);
        configColors();
    }

    public void setOutValue(double value) {
        String formattedValue = Formatador.formatarDouble(value);
        textViewUserOut.setText(formattedValue);
        configColors();
    }

    public double getInValue() {
        return Formatador.getDoubleFromFormattedValue(textViewUserIn.getText().toString());
    }

    public double getOutValue() {
        return Formatador.getDoubleFromFormattedValue(textViewUserOut.getText().toString());
    }

    public void setStateViews(boolean enabled) {
        textViewUserIn.setEnabled(enabled);
        textViewUserOut.setEnabled(enabled);
    }

    private void configColors() {
        double inValue = Formatador.getDoubleFromFormattedValue(textViewUserIn.getText().toString());
        double outValue = Formatador.getDoubleFromFormattedValue(textViewUserOut.getText().toString());

        if (outValue < inValue) {
            textViewUserOut.setTextColor(Color.RED);
        } else if (outValue == inValue) {
            textViewUserOut.setTextColor(Color.WHITE);
        } else {
            textViewUserOut.setTextColor(Color.GREEN);
        }
    }
}
