package com.razmus.daugiagijispirmas.Interface;

public interface ProgressNotifier {
    void setCurrentProgress(int progressValue);
    void fileFound(String name);
}
