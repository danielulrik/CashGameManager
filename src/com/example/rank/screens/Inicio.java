package com.example.rank.screens;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.*;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.filechooser.FileChooserDialog;
import com.example.rank.*;
import com.example.rank.adapter.SpinnerAdapter;
import com.example.rank.db.Persistence;
import com.example.rank.files.PortabilityManager;
import com.example.rank.model.Round;

import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 08/07/2015
 * Time: 16:40
 */
public class Inicio extends Activity {

    private Button buttonSign;
    private Button buttonCreate;
    private Button buttonIncludeNewUser;
    private Spinner spinner;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicial);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.title));
        spinner = (Spinner) findViewById(R.id.spinner_rounds);
        updateSpinner();

        buttonIncludeNewUser = (Button) findViewById(R.id.btn_include_new_user);
        buttonCreate = (Button) findViewById(R.id.btn_criar);
        buttonSign = (Button) findViewById(R.id.btn_entrar);

        setSignButtonListener();
        setCreateButtonListener();
        setIncludeUserListener();
    }

    @Override
    protected void onResume() {
        updateSpinner();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_importar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_importar) {
            DialogInterface.OnClickListener sim = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    importData();
                }
            };
            DialogFactory.make(this, getString(R.string.text_importar_informacoes), sim, null).show();
        } else if (item.getItemId() == android.R.id.home) {
            showDialogExitApp();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showDialogExitApp() {
        DialogFactory.make(this, getString(R.string.msg_sair_app), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, null).show();
    }

    private void importData() {
        final FileChooserDialog dialog = new FileChooserDialog(Inicio.this, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        dialog.setCanCreateFiles(false);
        dialog.setShowConfirmation(true, true);
        dialog.setFilter(".*rank");
        dialog.setShowOnlySelectable(true);
        dialog.setFolderMode(false);
        dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
            @Override
            public void onFileSelected(Dialog source, File file) {
                String filePath = file.getPath();
                if (!filePath.contains("round_")) {
                    Formatador.showMsg(Inicio.this, getString(R.string.msg_arquivo_invalido));
                } else {
                    if (importReceivedFile(filePath)) {
                        Formatador.showMsg(Inicio.this, getString(R.string.msg_importacao_sucesso));
                        dialog.dismiss();
                    } else {
                        Formatador.showMsg(Inicio.this, getString(R.string.msg_importacao_falha));
                    }
                }
            }

            @Override
            public void onFileSelected(Dialog source, File folder, String name) {
            }
        });
        dialog.show();
    }

    private boolean importReceivedFile(String file) {
        PortabilityManager portabilityManager = new PortabilityManager();
        try {
            portabilityManager.importInformationsFromZip(file, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
            updateSpinner();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void updateSpinner() {
        List<Round> rounds = Persistence.getInstance().getAllRounds();
        if (rounds.size() == 0) {
            rounds.add(new Round(-1L, null, null, 0.0));
        }
        spinner.setAdapter(new SpinnerAdapter(this, R.layout.spinner_adapter, rounds));
    }

    private void setIncludeUserListener() {
        buttonIncludeNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Inicio.this, AddNewUser.class));
            }
        });
    }

    private void setCreateButtonListener() {
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Inicio.this, AddNewRound.class));
            }
        });
    }

    private void setSignButtonListener() {
        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Persistence.getInstance().getAllRounds().isEmpty()) {
                    Toast.makeText(Inicio.this, getString(R.string.msg_sel_round_para_entrar), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Inicio.this, Main.class);
                    intent.putExtra("idRound", ((Round) spinner.getSelectedItem()).getId());
                    startActivity(intent);
                }
            }
        });
    }

}