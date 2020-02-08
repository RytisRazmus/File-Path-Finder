package com.razmus.daugiagijispirmas.Model;
@SuppressWarnings("WeakerAccess")
public class SearchData {

    public volatile int progress = 0;
    public volatile boolean finished = false;
    public volatile String dirName = "";

    public void reset(){
        progress = 0;
        finished = false;
        dirName = "";
    }
}
