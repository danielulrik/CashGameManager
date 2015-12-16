package com.example.rank.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 24/07/2015
 * Time: 15:13
 */
public class ZipUtil {

    private List<String> fileList;

    /**
     * Utilizar para descomprimir arquivo
     */
    public ZipUtil() {
        fileList = new ArrayList<>();
    }

    public void makeZip(String targetedFolder, String zipTo) throws IOException {
        generateFileList(new File(targetedFolder), targetedFolder);
        zipIt(zipTo, targetedFolder);
    }

    /**
     * Zip it
     *
     * @param pathToSave output ZIP file location
     */
    private void zipIt(String pathToSave, String pathToGetFilesToZip) throws IOException {
        byte[] buffer = new byte[1024];
        FileOutputStream fos = new FileOutputStream(pathToSave);
        ZipOutputStream zos = new ZipOutputStream(fos);

        for (String file : this.fileList) {

            ZipEntry ze = new ZipEntry(file);
            zos.putNextEntry(ze);
            FileInputStream in = new FileInputStream(pathToGetFilesToZip + File.separator + file);

            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            in.close();
        }

        zos.closeEntry();
        zos.close();
    }

    /**
     * Unzip it
     *
     * @param zipFile      input zip file
     * @param outputFolder zip file output folder
     */
    public void unZipIt(String zipFile, String outputFolder) throws IOException {
        byte[] buffer = new byte[1024];
        try {
            //create output directory is not exists
            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

        } catch (IOException ex) {
            throw ex;
        }
    }

    /**
     * Traverse a directory and get all files,
     * and add the file into fileList
     *
     * @param fileFolder file or directory
     */
    private void generateFileList(File fileFolder, String targetedFolder) {
        //add file only
        if (fileFolder.isFile()) {
            fileList.add(generateZipEntry(fileFolder.getAbsoluteFile().toString(), targetedFolder));
        }

        if (fileFolder.isDirectory()) {
            String[] subNote = fileFolder.list();
            for (String filename : subNote) {
                generateFileList(new File(fileFolder, filename), targetedFolder);
            }
        }
    }

    /**
     * Format the file path for zip
     *
     * @param file file path
     * @return Formatted file path
     */
    private String generateZipEntry(String file, String targetedFolder) {
        return file.substring(targetedFolder.length() + 1, file.length());
    }
}
