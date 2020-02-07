package com.razmus.daugiagijispirmas.Model;

import android.os.Environment;

import com.razmus.daugiagijispirmas.Interface.Searcher;

import java.io.File;

public class FileFetcher implements Runnable {

    private final SearchData search;
    private Searcher callback;
    private String directoryName = "";
    private String searchedName = "";
    private int dirCount = 0;
    private String defaultDir = "";


    public FileFetcher(SearchData search, Searcher callback) {
        this.search = search;
        this.callback = callback;
    }

    public void reset() {
        dirCount = 0;
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
            findFile(directoryName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        search.finished = true;

    }
    // I hate this
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

    public void findFile(String directoryName) throws Exception {

        File[] files = getFiles(directoryName);

        if (files != null) {
            for (int i = 0; i < files.length; i++) {


                String fileName = files[i].getName();
                String newPath = directoryName + "/" + fileName;
                if (files[i].isDirectory()) { // check if file is a directory
                    findFile(newPath);
                    if (directoryName.equals(defaultDir)) { // if true this means recursion is back to the main directory
                        search.progress++;
                    }
                } else if (fileName.contains(searchedName)) { // file found
                    search.dirName = newPath;
                }


            }
        }
    }

    private void checkFiles(File file) throws Exception{

    }

}
