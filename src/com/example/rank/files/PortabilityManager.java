package com.example.rank.files;

import android.util.Log;
import com.example.rank.db.Persistence;
import com.example.rank.model.Lancamento;
import com.example.rank.model.Round;
import com.example.rank.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by desenvolv09 on 23/07/2015.
 */
public class PortabilityManager {

    private ZipUtil zipUtil;
    private List<String> filesSaved;

    public PortabilityManager() {
        zipUtil = new ZipUtil();
        filesSaved = new ArrayList<>();
    }

    public boolean writeJsonOnDisk(String json, String fileName, String path) {
        try {
            File f = new File(path, fileName + ".json");
            filesSaved.add(path + "/" + fileName + ".json");
            FileWriter fw = new FileWriter(f);
            PrintWriter writer = new PrintWriter(fw);
            writer.println(json);
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            Log.e("error writing file", e.getMessage());
            return false;
        }
    }

    public void importInformationsFromZip(String zipName, String path) throws IOException {
        unzipFiles(zipName, path);

        String lancamentosJson = getJsonFromFile(path + "/lancamentos.json");
        String roundsJson = getJsonFromFile(path + "/round.json");
        String usersJson = getJsonFromFile(path + "/usuarios.json");

        filesSaved.add(path + "/lancamentos.json");
        filesSaved.add(path + "/round.json");
        filesSaved.add(path + "/usuarios.json");

        Gson gson = new Gson();
        List<Round> rounds = gson.fromJson(roundsJson, new TypeToken<List<Round>>() {
        }.getType());
        List<Usuario> usuarios = gson.fromJson(usersJson, new TypeToken<List<Usuario>>() {
        }.getType());
        List<Lancamento> lancamentos = gson.fromJson(lancamentosJson, new TypeToken<List<Lancamento>>() {
        }.getType());

        deleteExportedJsonFiles();
        deleteSavedZip(zipName);

        Persistence.getInstance().handleImportedEntities(rounds.get(0), usuarios, lancamentos);
    }

    public void unzipFiles(String nomeZip, String pastaAlvo) throws IOException {
        zipUtil.unZipIt(nomeZip, pastaAlvo);
    }

    public static void deleteSavedZip(String savedZipFile) {
        File f = new File(savedZipFile);
        if (f.delete()) {
            Log.i("rankfiles", "Zip deleted with success: " + savedZipFile);
        } else {
            Log.i("rankfiles", "Failed to delete: " + savedZipFile);
        }
    }

    private String getJsonFromFile(String fileName) throws IOException {
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        File f = new File(fileName);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
        while ((str = bufferedReader.readLine()) != null) {
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    public void zipFiles(String pastaAlvo, String zipTo) throws IOException {
        zipUtil.makeZip(pastaAlvo, zipTo);
    }

    public void deleteExportedJsonFiles() {
        if (!filesSaved.isEmpty()) {
            for (String s : filesSaved) {
                File f = new File(s);
                if (f.delete()) {
                    Log.i("rankfiles", "Deleted with success: " + s);
                } else {
                    Log.i("rankfiles", "Failed to delete: " + s);
                }
            }
        }
    }
}
