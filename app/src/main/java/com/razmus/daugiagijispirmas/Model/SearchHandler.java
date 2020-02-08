package com.razmus.daugiagijispirmas.Model;

import com.razmus.daugiagijispirmas.Interface.ProgressNotifier;
import com.razmus.daugiagijispirmas.Interface.Searcher;


public class SearchHandler {

    private final SearchData search = new SearchData();
    private FileFinder fileFinder;
    private ProgressObserver progressObserver;
    private Thread finderThread;
    private Thread progressThread;

    public SearchHandler(Searcher callback, ProgressNotifier progressNotifier) {
        fileFinder = new FileFinder(search, callback);
        progressObserver = new ProgressObserver(search, progressNotifier);
    }

    public void search(String directoryName, String searchedName) {
        reset();
        fileFinder.setSearchParameters(directoryName, searchedName);
        finderThread = new Thread(fileFinder);
        progressThread = new Thread(progressObserver);
        finderThread.start();
        progressThread.start();
    }

    public void reset() {
        if (finderThread != null && progressThread != null) {
            finderThread.interrupt();
            progressThread.interrupt();
        }
        progressObserver.reset();
        search.reset();
    }

}
