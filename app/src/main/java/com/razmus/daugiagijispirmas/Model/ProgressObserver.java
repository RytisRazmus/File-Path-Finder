package com.razmus.daugiagijispirmas.Model;


import com.razmus.daugiagijispirmas.Interface.ProgressNotifier;

public class ProgressObserver implements Runnable {

    private final SearchData search;
    private ProgressNotifier callback;
    private int oldProgressValue = 0;
    private String oldDirName = "";

    public ProgressObserver(SearchData search, ProgressNotifier callback){
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