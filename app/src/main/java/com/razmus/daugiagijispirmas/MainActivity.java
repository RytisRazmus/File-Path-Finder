package com.razmus.daugiagijispirmas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.razmus.daugiagijispirmas.Adapters.DirectoryAdapter;
import com.razmus.daugiagijispirmas.Interface.Searcher;
import com.razmus.daugiagijispirmas.Model.FileFinder;
import com.razmus.daugiagijispirmas.Interface.ProgressChecker;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Searcher, ProgressChecker {

    private final int REQUEST_CODE = 1;
    private RecyclerView searchResultsRecycler;
    private EditText dirSearchEditText;
    private EditText fileSearchEditText;
    private Button searchButton;
    private SeekBar progressSeekBar;
    private DirectoryAdapter directoryAdapter;
    private FileFinder finder = new FileFinder(this, this);
    private ArrayList<String> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: add permissionCheck
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE);

        findViews();
        setRecycler();
        setOnClicks();
        progressSeekBar.setEnabled(false);
    }

    private void findViews(){
        searchResultsRecycler = findViewById(R.id.serachrecycler);
        searchButton = findViewById(R.id.searchButton);
        dirSearchEditText = findViewById(R.id.dirSearchEditText);
        fileSearchEditText = findViewById(R.id.fileSearchEditText);
        progressSeekBar = findViewById(R.id.progresSeekBar);
    }

    private void setRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        searchResultsRecycler.setLayoutManager(layoutManager);
        directoryAdapter = new DirectoryAdapter(results);
        searchResultsRecycler.setAdapter(directoryAdapter);
    }

    private void setOnClicks(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }

    private void resetSearch() {
        results.clear();
        directoryAdapter.notifyDataSetChanged();
        progressSeekBar.setProgress(0);
        finder.reset();
    }

    private void search(){
        resetSearch();
        Thread thread = new Thread(){
            public void run(){

                String dirName = dirSearchEditText.getText().toString();
                String fileName = fileSearchEditText.getText().toString();
                finder.test(dirName, fileName);
            }
        };
        thread.start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission to External storage granted", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void fileFound(final String name) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                results.add(name);
                directoryAdapter.notifyDataSetChanged();
                Log.d("filefound", name);
            }
        });
    }

    @Override
    public void setCurrentProgress(final int progressValue) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressSeekBar.setProgress(progressValue);
                Log.d("threadEnd", "setting value " + progressSeekBar.getMax() + " " + progressSeekBar.getProgress());
            }
        });
    }

    @Override
    public void setMaxProgress(int maxProgress) {
        progressSeekBar.setMax(maxProgress);
    }

}
