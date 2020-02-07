package com.razmus.daugiagijispirmas.Model;


import com.razmus.daugiagijispirmas.Interface.ProgressChecker;

public class Progress implements Runnable {

    private final SearchData search;
    private ProgressChecker callback;
    private int oldProgressValue = 0;
    private String oldDirName = "";

    public Progress(SearchData search, ProgressChecker callback){
        this.search = search;
        this.callback = callback;
    }

    public void reset(){
        oldProgressValue = 0;
        oldDirName = "";
    }

    @Override
    public void run() {

        while ( !search.finished ) {
            if (oldProgressValue != search.progress) {
                callback.setCurrentProgress(search.progress);
                oldProgressValue = search.progress;
            }

            if (oldDirName != search.dirName){
                callback.fileFound(search.dirName);
                oldDirName = search.dirName;
            }
        }

    }
}