package com.razmus.daugiagijispirmas.Model;

import com.razmus.daugiagijispirmas.Interface.ProgressNotifier;

@SuppressWarnings("WeakerAccess")

public class ProgressObserver implements Runnable {

    private final SearchData search;
    private ProgressNotifier callback;
    private int oldProgressValue = 0;
    private String oldDirName = "";

    public ProgressObserver(SearchData search, ProgressNotifier callback) {
        this.search = search;
        this.callback = callback;
    }

    public void reset() {
        oldProgressValue = 0;
        oldDirName = "";
    }

    @Override
    public void run() {
        while (!search.finished) {
            checkProgress();
            checkForNewDir();
        }
        callback.searchFinished();
    }

    private void checkProgress() {
        if (oldProgressValue != search.progress) {
            callback.setCurrentProgress(search.progress);
            oldProgressValue = search.progress;
        }
    }

    private void checkForNewDir(){
        if (!oldDirName.equals(search.dirName)) {
            callback.fileFound(search.dirName);
            oldDirName = search.dirName;
        }
    }

}