package com.example.rank;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.example.filechooser.FileChooserDialog;
import com.example.rank.component.Abas;
import com.example.rank.db.Persistence;
import com.example.rank.files.PortabilityManager;
import com.example.rank.model.Lancamento;
import com.example.rank.model.Round;
import com.example.rank.model.Usuario;
import com.example.rank.screens.FragmentPeriod;
import com.example.rank.screens.FragmentAddNewBalance;
import com.example.rank.screens.FragmentRank;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main extends FragmentActivity {

    private static final int REQUEST_EXPORT_DATA = 9;
    private Type typeRound = new TypeToken<List<Round>>() {
    }.getType();
    private Type typeUser = new TypeToken<List<Usuario>>() {
    }.getType();
    private Type typeLancamentos = new TypeToken<List<Lancamento>>() {
    }.getType();

    private int selectedRound = -1;

    private FragmentPeriod fragmentPeriod;
    private FragmentRank fragmentRank;
    private FragmentAddNewBalance fragmentAddNewBalance;
    private Abas abas;
    private String sharedFileName = "";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle(getString(R.string.title_app));

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        selectedRound = (int) getIntent().getLongExtra("idRound", 0);

        if (savedInstanceState != null) {
            selectedRound = (int) savedInstanceState.get("selectedRound");
        }

        fragmentPeriod = new FragmentPeriod();
        fragmentRank = new FragmentRank();
        fragmentAddNewBalance = new FragmentAddNewBalance();

        abas = new Abas();

        abas.addTitulo(getString(R.string.text_period), getString(R.string.text_lancamentos), getString(R.string.text_ranl));
        abas.addFragmento(fragmentPeriod, fragmentAddNewBalance, fragmentRank);
        abas.construir(getSupportFragmentManager(), (ViewPager) findViewById(R.id.viewPager), getActionBar());
    }

    public void setPositionViewPager(int position) {
        abas.setCurrentItemViewPager(position);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedRound", selectedRound);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exportar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_exportar) {
            treatMenuExportedPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void treatMenuExportedPressed() {
        DialogInterface.OnClickListener listenerLocalExport = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final FileChooserDialog fileChooserDialog = new FileChooserDialog(Main.this);
                fileChooserDialog.setFolderMode(true);
                fileChooserDialog.setCanCreateFiles(false);
                fileChooserDialog.setShowConfirmation(true, false);
                fileChooserDialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
                    @Override
                    public void onFileSelected(Dialog source, File file) {
                        try {
                            exportData(file.getPath() + "/" + generateExportedFileName());
                            Formatador.showMsg(Main.this, "Arquivo exportado com sucesso.");
                            fileChooserDialog.dismiss();
                        } catch (IOException e) {
                            Formatador.showMsg(Main.this, "Falha ao exportar arquivo.");
                        }
                    }

                    @Override
                    public void onFileSelected(Dialog source, File folder, String name) {
                    }
                });
                fileChooserDialog.show();
            }
        };
        DialogInterface.OnClickListener listenerShareData = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String exportedTo = exportData(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/" + generateExportedFileName());
                    String fileName = "file:///" + exportedTo;
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileName));
                    sendIntent.setType("application/zip");
                    sharedFileName = exportedTo;
                    startActivityForResult(sendIntent, REQUEST_EXPORT_DATA);
                } catch (IOException e) {
                    Formatador.showMsg(Main.this, "Falha ao exportar arquivo.");
                }
            }
        };
        DialogFactory.make(this, getString(R.string.text_exportar), "O que deseja fazer?",
                listenerShareData, listenerLocalExport, "Compartilhar", "Exportar").show();
    }

    private String exportData(String pathToSave) throws IOException {
        List<Round> rounds = new ArrayList<>();
        rounds.add(Persistence.getInstance().getRoundById(selectedRound));
        List<Lancamento> lancamentos = Persistence.getInstance().getLancamentosByRoundUser(selectedRound);
        List<Usuario> users = Persistence.getInstance().getUsersByRound(selectedRound);

        Gson gson = new Gson();

        String jsonUsuarios = gson.toJson(users, typeUser);
        String jsonRound = gson.toJson(rounds, typeRound);
        String jsonLancamentos = gson.toJson(lancamentos, typeLancamentos);

        String path = getExternalFilesDir(null).getPath();

        PortabilityManager portabilityManager = new PortabilityManager();
        portabilityManager.writeJsonOnDisk(jsonRound, "round", path);
        portabilityManager.writeJsonOnDisk(jsonLancamentos, "lancamentos", path);
        portabilityManager.writeJsonOnDisk(jsonUsuarios, "usuarios", path);
        portabilityManager.zipFiles(path, pathToSave);
        portabilityManager.deleteExportedJsonFiles();

        return pathToSave;
    }

    private String generateExportedFileName() {
        Round round = Persistence.getInstance().getRoundById(selectedRound);
        String iniDate = Formatador.formatarDataFileName(round.getIniDate());
        String endDate = Formatador.formatarDataFileName(round.getEndDate());
        return "round_" + iniDate + "_a_" + endDate + ".rank";
    }

    public int getSelectedRound() {
        return selectedRound;
    }

    public void updateViews() {
        fragmentRank.atualizar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EXPORT_DATA) {
            if (!sharedFileName.isEmpty()) {
                PortabilityManager.deleteSavedZip(sharedFileName);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
