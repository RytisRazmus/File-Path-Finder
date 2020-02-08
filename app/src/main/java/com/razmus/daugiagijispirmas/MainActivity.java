package com.razmus.daugiagijispirmas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.razmus.daugiagijispirmas.Adapters.DirectoryAdapter;
import com.razmus.daugiagijispirmas.Interface.Searcher;
import com.razmus.daugiagijispirmas.Model.SearchHandler;
import com.razmus.daugiagijispirmas.Interface.ProgressNotifier;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements Searcher, ProgressNotifier {

    private final int REQUEST_CODE = 1;
    private RecyclerView searchResultsRecycler;
    private EditText dirSearchEditText;
    private EditText fileSearchEditText;
    private Button searchButton;
    private SeekBar progressSeekBar;
    private TextView timeTextView;
    private DirectoryAdapter directoryAdapter;
    private SearchHandler finder = new SearchHandler(this, this);
    private ArrayList<String> results = new ArrayList<>();
    private final String searchFinishedText = "Paieška baigta.";
    private double startTime = 0;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestStoragePerission();
        findViews();
        setRecycler();
        setOnClicks();
        progressSeekBar.setEnabled(false);
    }

    private void requestStoragePerission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE);
    }

    private void findViews() {
        searchResultsRecycler = findViewById(R.id.serachrecycler);
        searchButton = findViewById(R.id.searchButton);
        dirSearchEditText = findViewById(R.id.dirSearchEditText);
        fileSearchEditText = findViewById(R.id.fileSearchEditText);
        progressSeekBar = findViewById(R.id.progresSeekBar);
        timeTextView = findViewById(R.id.timeTextView);
    }

    private void setRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        searchResultsRecycler.setLayoutManager(layoutManager);
        directoryAdapter = new DirectoryAdapter(results);
        searchResultsRecycler.setAdapter(directoryAdapter);
    }

    private void setOnClicks() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryToSearch();
            }
        });
    }

    private void resetSearch() {
        results.clear();
        directoryAdapter.notifyDataSetChanged();
        progressSeekBar.setProgress(0);
        finder.reset();
    }

    private void tryToSearch() {
        if (checkStoragePermission()) {
            search();
        } else {
            requestStoragePerission();
        }
    }

    private void search() {
        resetSearch();
        String dirName = dirSearchEditText.getText().toString();
        String fileName = fileSearchEditText.getText().toString();
        startTimer();
        finder.search(dirName, fileName);
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        long interval = 100;
        startTime = System.currentTimeMillis();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTimeTextViewText();
                    }
                });
            }
        }, 0, interval);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void setTimeTextViewText() {
        DecimalFormat form = new DecimalFormat("0.00");
        double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.00;
        String text = form.format(elapsedTime) + "s";
        timeTextView.setText(text);
    }

    private boolean checkStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            return false;
        }
        return true;
    }

    private void addResult(String name) {
        results.add(0, name);
        directoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void fileFound(final String name) {
        Log.d("rezultatas", name);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                addResult(name);
            }
        });
    }

    @Override
    public void searchFinished() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                stopTimer();
                Toast.makeText(getApplicationContext(), searchFinishedText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setCurrentProgress(final int progressValue) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressSeekBar.setProgress(progressValue);
            }
        });
    }

    @Override
    public void setMaxProgress(int maxProgress) {
        progressSeekBar.setMax(maxProgress);
    }

    //hide keyboard on screen touch
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}
