package com.example.rank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.rank.Formatador;
import com.example.rank.R;
import com.example.rank.model.Round;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 08/07/2015
 * Time: 16:48
 */
public class SpinnerAdapter extends ArrayAdapter<Round> {

    private int resource;
    private Context context;

    public SpinnerAdapter(Context context, int resource, List<Round> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getConvertView(convertView, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getConvertView(convertView, position);
    }

    private View getConvertView(View convertView, int position) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.text1);
        Round campo = getItem(position);
        if (campo.getId() == -1) {
            textView.setText(context.getString(R.string.msg_create_new_round));
        } else {
            textView.setText(campo.getId() + context.getString(R.string.text_spinner_adapter) + "\n" + Formatador.formatarData(campo.getIniDate()) + " a " + Formatador.formatarData(campo.getEndDate()));
        }
        return convertView;
    }
}
