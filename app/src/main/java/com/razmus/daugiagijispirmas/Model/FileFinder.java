package com.razmus.daugiagijispirmas.Model;

import com.razmus.daugiagijispirmas.Interface.ProgressChecker;
import com.razmus.daugiagijispirmas.Interface.Searcher;


public class FileFinder {

    private final SearchData search = new SearchData();
    private FileFetcher mFileFetcher;
    private Progress mProgress;
    private Thread finderThred;
    private Thread progressThread;

    public FileFinder(Searcher callback, ProgressChecker progressChecker){
        mFileFetcher = new FileFetcher(search, callback);
        mProgress = new Progress(search, progressChecker);
    }

    public void search(String directoryName, String searchedName) {
        reset();
        mFileFetcher.setSearchParameters(directoryName, searchedName);
        finderThred = new Thread(mFileFetcher);
        progressThread = new Thread(mProgress);
        finderThred.start();
        progressThread.start();
    }

    public void reset(){
        if (finderThred != null && progressThread != null){
            finderThred.interrupt();
            progressThread.interrupt();
        }
        mFileFetcher.reset();
        mProgress.reset();
        search.reset();
    }

}
