package com.razmus.daugiagijispirmas.Model;

import android.os.Environment;

import com.razmus.daugiagijispirmas.Interface.Searcher;

import java.io.File;

@SuppressWarnings("WeakerAccess")

public class FileFinder implements Runnable {

    private final SearchData search;
    private Searcher callback;
    private String searchedName = "";
    private String startingDir = "";


    public FileFinder(SearchData search, Searcher callback) {
        this.search = search;
        this.callback = callback;
    }

    public void setSearchParameters(String directoryName, String searchedName) {
        startingDir = directoryName;
        this.searchedName = searchedName;
    }

    @Override
    public void run() {
        try {
            int dirCount = getDirCountInStartingDirectory();
            callback.setMaxProgress(dirCount);
            findFile(startingDir);

        } catch (Exception e) {
            e.printStackTrace();
        }
        search.finished = true;

    }
    // I hate this
    private int getDirCountInStartingDirectory() throws Exception{
        int dirCount = 0;
        File[] files = getFiles(startingDir);
        for (File file : files) {
            if (file.isDirectory()) {
                dirCount++;
            }
        }
        return dirCount;
    }

    private File[] getFiles(String directoryName) throws Exception {
        String path = Environment.getExternalStorageDirectory().toString() + directoryName;
        File directory = new File(path);

        File[] files = directory.listFiles();
        if (files == null) {
            throw new Exception("No such directory");
        }
        return files;
    }

    private void findFile(String dirName) throws Exception {
        File[] files = getFiles(dirName);
        if (files != null) {
            for (File file: files) {
                checkFileType(file, dirName);
            }
        }
    }

    private void checkFileType(File file, String dirName) throws Exception{
        String fileName = file.getName();
        String newPath = dirName + fileName + "/";
        if (file.isDirectory()) { // check if file is a directory
            findFile(newPath);
            if (dirName.equals(startingDir)) { // if true this means recursion is back to the main directory
                synchronized (search){
                    search.progress++;
                }
            }
        }
        if (fileName.contains(searchedName)) { // file found
            search.dirName = dirName;
        }
    }

}
