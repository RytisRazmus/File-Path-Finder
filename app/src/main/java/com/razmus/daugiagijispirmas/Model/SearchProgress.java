package com.razmus.daugiagijispirmas.Model;

public class SearchProgress {
    public volatile int progress = 0;
    public volatile boolean finished = false;

    public void reset(){
        progress = 0;
        finished = false;
    }
}
