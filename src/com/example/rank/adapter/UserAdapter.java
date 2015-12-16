package com.example.rank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import com.example.rank.R;
import com.example.rank.model.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 16/07/2015
 * Time: 09:37
 */
public class UserAdapter extends BaseAdapter implements Filterable {

    private HashMap<String, Boolean> checkedUsers;

    private Context context;
    private List<Usuario> users;
    private List<Usuario> unAlteredUsers;
    private int resource;

    public UserAdapter(Context context, int resource, List<Usuario> users) {
        this.context = context;
        this.resource = resource;
        this.users = users;
        unAlteredUsers = users;

        if (checkedUsers == null) {
            checkedUsers = new HashMap<>();
        }
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        final Usuario user = users.get(position);
        final CheckBox checkBox = ((CheckBox) convertView.findViewById(R.id.check_box_user_name));

        checkBox.setText(user.getNome());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedUsers.put(user.getNome(), checkBox.isChecked());
            }
        });

        if (checkedUsers.get(user.getNome()) == null) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(checkedUsers.get(user.getNome()));
        }

        return convertView;
    }

    public HashMap<String, Boolean> getCheckedUsers() {
        return checkedUsers;
    }

    @Override
    public Filter getFilter() {
        return new UserFilter();
    }

    class UserFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                filterResults.count = users.size();
                filterResults.values = unAlteredUsers;
            } else {
                List<Usuario> filteredList = new ArrayList<>();
                for (Usuario user : users) {
                    if (user.getNome().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        filteredList.add(user);
                    }
                }
                filterResults.count = filteredList.size();
                filterResults.values = filteredList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            users = (List<Usuario>) results.values;
            notifyDataSetChanged();
        }
    }
}
