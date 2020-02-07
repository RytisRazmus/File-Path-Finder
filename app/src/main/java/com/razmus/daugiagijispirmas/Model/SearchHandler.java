package com.razmus.daugiagijispirmas.Model;

import com.razmus.daugiagijispirmas.Interface.ProgressNotifier;
import com.razmus.daugiagijispirmas.Interface.Searcher;


public class SearchHandler {

    private final SearchData search = new SearchData();
    private FileFetcher mFileFetcher;
    private ProgressObserver mProgressObserver;
    private Thread finderThread;
    private Thread progressThread;

    public SearchHandler(Searcher callback, ProgressNotifier progressNotifier) {
        mFileFetcher = new FileFetcher(search, callback);
        mProgressObserver = new ProgressObserver(search, progressNotifier);
    }

    public void search(String directoryName, String searchedName) {
        reset();
        mFileFetcher.setSearchParameters(directoryName, searchedName);
        finderThread = new Thread(mFileFetcher);
        progressThread = new Thread(mProgressObserver);
        finderThread.start();
        progressThread.start();
    }

    public void reset() {
        if (finderThread != null && progressThread != null) {
            finderThread.interrupt();
            progressThread.interrupt();
        }
        mFileFetcher.reset();
        mProgressObserver.reset();
        search.reset();
    }

}
