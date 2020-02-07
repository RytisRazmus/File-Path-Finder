package com.razmus.daugiagijispirmas.Model;

import android.os.Environment;
import android.util.Log;

import com.razmus.daugiagijispirmas.Interface.Searcher;

import java.io.File;

public class Finder implements Runnable {

    private final SearchProgress search;
    private Searcher callback;
    private String directoryName = "";
    private String searchedName = "";
    private int dirCount = 0;
    private String defaultDir = "";


    public Finder(SearchProgress search, Searcher callback) {
        this.search = search;
        this.callback = callback;
    }

    public void reset() {
        dirCount = 0;
        defaultDir = "";
    }


    public void setSearchParameters(String directoryName, String searchedName) {
        this.directoryName = directoryName;
        this.searchedName = searchedName;
        defaultDir = directoryName;
    }

    @Override
    public void run() {

        try {
            setMaxProgress();
            findFile(directoryName, searchedName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        search.finished = true;

    }

    private void setMaxProgress() throws Exception {
        File[] files = getFiles(directoryName);
        for (File file : files) {
            if (file.isDirectory()) {
                dirCount++;
            }
        }
        callback.setMaxProgress(dirCount);
    }

    private File[] getFiles(String directoryName) throws Exception {
        String path = Environment.getExternalStorageDirectory().toString() + directoryName;
        File directory = new File(path);
        if (directory == null) {
            throw new Exception("No such directory");
        }
        File[] files = directory.listFiles();
        return files;
    }

    public void findFile(String directoryName, String searchedName) throws Exception {

        File[] files = getFiles(directoryName);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {

                String fileName = files[i].getName();

                if (files[i].isDirectory()) {
                    String newPath = directoryName + "/" + files[i].getName();
                    findFile(newPath, searchedName);
                    if (directoryName.equals(defaultDir)) {
                        search.progress++;
                        Log.d("directory", newPath + " size: " + dirCount);
                    }
                } else if (fileName.contains(searchedName)) {
                    Log.d("foundFile", directoryName);
                    callback.fileFound(directoryName + "/" + fileName);
                }

            }
        }
    }

}
