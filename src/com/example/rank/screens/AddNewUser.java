package com.example.rank.screens;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.rank.Formatador;
import com.example.rank.R;
import com.example.rank.db.Persistence;
import com.example.rank.model.Usuario;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 16/07/2015
 * Time: 09:40
 */
public class AddNewUser extends Activity implements View.OnClickListener {

    private EditText editTextUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_user_activity);

        setTitle(getString(R.string.create_new_user));

        editTextUser = (EditText) findViewById(R.id.edit_new_user);
        findViewById(R.id.button_save_user).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Usuario usuario = new Usuario();
        usuario.setNome(editTextUser.getText().toString());

        if (v.getId() == R.id.button_save_user) {
            if (!editTextUser.getText().toString().isEmpty()) {
                saveUser(usuario);
            } else {
                Formatador.showMsg(this, getString(R.string.msg_user_empty));
            }
        }
    }

    private void saveUser(Usuario usuario) {
        if (Persistence.getInstance().containsUser(usuario)) {
            editTextUser.selectAll();
            Formatador.showMsg(this, getString(R.string.user_already_in_db));
        } else {
            Persistence.getInstance().saveUser(usuario);
            Formatador.showMsg(this, getString(R.string.msg_user_saved));
            finish();
        }
    }
}