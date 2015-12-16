package com.example.rank;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 09/07/2015
 * Time: 09:41
 */
public class Formatador {

    public static String formatarDouble(double valor) {
        return "R$ " + String.format("%.2f", valor);
    }

    public static String formatarDataFileName(Date date) {
        return formatDate(date, "dd-MM-yyyy");
    }

    public static String formatarData(Date data) {
        return formatDate(data, "dd/MM/yyyy");
    }

    private static String formatDate(Date data, String format) {
        return new SimpleDateFormat(format).format(data);
    }

    public static Date getDateFromString(String date) {
        Date d;

        try {
            d = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            d = null;
        }

        return d;
    }

    public static double getDoubleFromFormattedValue(String valor) {
        if (valor.isEmpty()) {
            return 0.0;
        }
        if (valor.contains("R$")) {
            String valorPuro = valor.replace("R$ ", "");
            if (valor.contains(",")) {
                valorPuro = valorPuro.replace(",", ".");
                return Double.parseDouble(valorPuro);
            }
        }
        return Double.parseDouble(valor);
    }

    public static void showMsg(Context context, String msg, int duration) {
        Toast t = Toast.makeText(context, msg, duration);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    public static void showMsg(Context context, String msg) {
        Toast t = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

}
