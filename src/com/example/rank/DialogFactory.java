package com.example.rank;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 20/07/2015
 * Time: 09:41
 */
public class DialogFactory {

    public static AlertDialog make(Context context, String msg) {
        return new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Attention")
                .setMessage(msg)
                .setPositiveButton("Close", null)
                .create();
    }

    public static AlertDialog make(Context context, String msg, String title) {
        return new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Close", null)
                .create();
    }

    public static AlertDialog make(Context context, String msg, DialogInterface.OnClickListener yesListener) {
        return new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(context.getString(R.string.text_atencao))
                .setMessage(msg)
                .setPositiveButton("Ok", yesListener)
                .create();
    }

    public static AlertDialog make(Context context, String msg, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
        return new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(context.getString(R.string.text_atencao))
                .setMessage(msg)
                .setPositiveButton("Yes", yesListener)
                .setNegativeButton("No", noListener)
                .create();
    }

    public static AlertDialog make(Context context, String title, String msg, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
        return new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Yes", yesListener)
                .setNegativeButton("No", noListener)
                .create();
    }

    public static AlertDialog make(Context context, String title, String msg, DialogInterface.OnClickListener yesListener,
                                   DialogInterface.OnClickListener noListener, String textYes, String textNo) {
        return new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(textYes, yesListener)
                .setNegativeButton(textNo, noListener)
                .create();
    }

}
