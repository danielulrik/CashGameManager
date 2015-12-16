package com.example.rank.screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.rank.DialogFactory;
import com.example.rank.Formatador;
import com.example.rank.Main;
import com.example.rank.R;
import com.example.rank.adapter.UserAdapter;
import com.example.rank.component.SimpleDatePickerDialog;
import com.example.rank.db.Persistence;
import com.example.rank.model.Round;
import com.example.rank.model.Usuario;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 09/07/2015
 * Time: 09:39
 */
public class AddNewRound extends Activity implements View.OnClickListener {

    private EditText edtTaxa;
    private EditText edtIni;
    private EditText edtFim;
    private RelativeLayout layoutPickUsers;
    private UserAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_period);
        setTitle(getString(R.string.title_new_round));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(getString(R.string.text_pick_to_participate));
        dialog.setContentView(layoutPickUsers);
        findViewById(R.id.img_button_add_users).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutPickUsers.findViewById(R.id.btn_finished).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView textViewMembers = ((TextView) findViewById(R.id.count_members));
                        textViewMembers.setText(getString(R.string.text_total_of) + getSelectedUsers().size() + "\n" + getString(R.string.text_click_visualize));
                        textViewMembers.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StringBuilder sb = new StringBuilder();
                                for (Usuario usuario : getSelectedUsers()) {
                                    if (getSelectedUsers().indexOf(usuario) == 0) {
                                        sb.append("- ").append(usuario.getNome());
                                    } else {
                                        sb.append("\n").append("- ").append(usuario.getNome());
                                    }
                                }
                                DialogFactory.make(AddNewRound.this, sb.toString(), getString(R.string.text_participants)).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });

                if (Persistence.getInstance().getAllUsuarios().size() > 1) {
                    dialog.show();
                } else {
                    Toast.makeText(AddNewRound.this, getString(R.string.msg_at_least_two), Toast.LENGTH_SHORT).show();
                }
            }
        });

        configSearchView();
    }

    private void configSearchView() {
        SearchView searchView = (SearchView) layoutPickUsers.findViewById(R.id.search_view_users);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter.getFilter().filter("");
                return false;
            }
        });
    }

    private void initialize() {
        edtIni = (EditText) findViewById(R.id.editDataIni);
        edtFim = (EditText) findViewById(R.id.editDataFim);
        edtTaxa = (EditText) findViewById(R.id.editTaxa);
        edtIni.setOnClickListener(this);
        edtFim.setOnClickListener(this);
        findViewById(R.id.btn_salvar_round).setOnClickListener(this);
        findViewById(R.id.btn_selecionar_existente).setOnClickListener(this);
        layoutPickUsers = (RelativeLayout) LayoutInflater.from(AddNewRound.this).inflate(R.layout.view_pick_users_list, null);
        ListView listViewUsers = (ListView) layoutPickUsers.findViewById(R.id.list_view_users);
        adapter = new UserAdapter(this, R.layout.user_adapter, Persistence.getInstance().getAllUsuarios());
        listViewUsers.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        final SimpleDatePickerDialog simpleDatePickerDialog = new SimpleDatePickerDialog(this);
        if (v.getId() == R.id.btn_salvar_round) {
            saveRound();
        } else if (v.getId() == R.id.btn_selecionar_existente) {
            startActivity(new Intent(AddNewRound.this, Inicio.class));
            finish();
        } else if (v.getId() == R.id.editDataIni) {
            simpleDatePickerDialog.createDialog().show();
            simpleDatePickerDialog.setOnSelectedDate(new SimpleDatePickerDialog.OnSelectedDate() {
                @Override
                public void selectedDate(Date selectedDate) {
                    edtIni.setText(Formatador.formatarData(selectedDate));
                }
            });
        } else if (v.getId() == R.id.editDataFim) {
            simpleDatePickerDialog.createDialog().show();
            simpleDatePickerDialog.setOnSelectedDate(new SimpleDatePickerDialog.OnSelectedDate() {
                @Override
                public void selectedDate(Date selectedDate) {
                    edtFim.setText(Formatador.formatarData(selectedDate));
                }
            });
        }
    }

    private void saveRound() {
        if (edtIni.getText().toString().isEmpty() || edtFim.getText().toString().isEmpty() || edtTaxa.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_todos_campos_nao_preenchidos), Toast.LENGTH_SHORT).show();
        } else {
            List<Usuario> checkedUsers = getSelectedUsers();
            if (checkedUsers.size() > 1) {
                Round round = new Round(Formatador.getDateFromString(edtIni.getText().toString()),
                        Formatador.getDateFromString(edtFim.getText().toString()),
                        Double.parseDouble(edtTaxa.getText().toString()), checkedUsers);

                if (round.getEndDate().before(round.getIniDate())) {
                    Toast.makeText(this, getString(R.string.msg_data_ini_menor), Toast.LENGTH_LONG).show();
                } else {
                    createNewRound(round);
                }
            } else {
                Toast.makeText(this, getString(R.string.msg_at_least_two), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<Usuario> getSelectedUsers() {
        HashMap<String, Boolean> checkedUsers = adapter.getCheckedUsers();
        List<Usuario> selectedUsers = new LinkedList<>();
        if (!checkedUsers.isEmpty()) {
            for (Usuario usuario : Persistence.getInstance().getAllUsuarios()) {
                if (checkedUsers.get(usuario.getNome()) != null) {
                    if (checkedUsers.get(usuario.getNome())) {
                        selectedUsers.add(usuario);
                    }
                }
            }
        }
        return selectedUsers;
    }

    private void createNewRound(Round round) {
        long id = Persistence.getInstance().createNewRound(round);
        Intent intent = new Intent(AddNewRound.this, Main.class);
        intent.putExtra("idRound", id);
        startActivity(intent);
        finish();
    }

}