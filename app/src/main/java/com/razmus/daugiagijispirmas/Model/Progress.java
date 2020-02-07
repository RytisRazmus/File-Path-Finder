package com.razmus.daugiagijispirmas.Model;


import com.razmus.daugiagijispirmas.Interface.ProgressChecker;

public class Progress implements Runnable {

    private final SearchProgress search;
    private ProgressChecker callback;
    private int oldValue = 0;

    public Progress(SearchProgress search, ProgressChecker callback){
        this.search = search;
        this.callback = callback;
    }

    public void reset(){
        oldValue = 0;
    }

    @Override
    public void run() {

        while ( !search.finished ) {
            if (oldValue != search.progress) {
                callback.setCurrentProgress(search.progress);
                oldValue = search.progress;
            }
        }

    }
}