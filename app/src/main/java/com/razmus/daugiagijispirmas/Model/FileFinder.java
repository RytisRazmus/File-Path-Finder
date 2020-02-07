package com.razmus.daugiagijispirmas.Model;

import com.razmus.daugiagijispirmas.Interface.ProgressChecker;
import com.razmus.daugiagijispirmas.Interface.Searcher;


public class FileFinder {

    private final SearchProgress search = new SearchProgress();
    private Finder finder;
    private Progress progress;
    private Thread finderThred;
    private Thread progressThread;

    public FileFinder(Searcher callback, ProgressChecker progressChecker){
        finder = new Finder(search, callback);
        progress = new Progress(search, progressChecker);
    }

    public void test(String directoryName, String searchedName) {
        reset();
        finder.setSearchParameters(directoryName, searchedName);
        finderThred = new Thread(finder);
        progressThread = new Thread(progress);
        finderThred.start();
        progressThread.start();
    }

    public void reset(){
        if (finderThred != null && progressThread != null){
            finderThred.interrupt();
            progressThread.interrupt();
        }
        finder.reset();
        progress.reset();
        search.reset();
    }

}
