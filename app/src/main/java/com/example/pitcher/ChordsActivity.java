package com.example.pitcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.HashMap;

public class ChordsActivity extends AppCompatActivity {

    NumberPicker rootPicker;
    NumberPicker chordPicker;
    String[] notes = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    String[] chordNames = {"M", "m", "dim", "M7", "m7", "7", "°7", "ø7"};
    double[] rootfreqs = new double[12];
    final double HALFSTEP = Math.pow(2, (double) 1/12);
    ArrayList<PlaySound> activeNotes = new ArrayList<>();

    double[] major = {1, 3.0/2, 2, 2.0 * 5/4};
    double[] minor = {1, 3.0/2, 2, 2.0 * 6/5};
    double[] dim = {1, 64.0/45, 2, 2.0 * 6/5};
    double[] major7 = {1, 3.0/2, 15.0/8, 2.0 * 5/4};
    double[] minor7 = {1, 3.0/2, 9.0/5, 2.0 * 6/5};
    double[] dom7 = {1, 3.0/2, 9.0/5, 2.0 * 5/4};
    double[] dim7 = {1, 64.0/45, 5.0/3, 2.0 * 6/5};
    double[] hdim7 = {1, 64.0/45, 9.0/5, 2.0 * 6/5};

    double[][] chordRatios = {major, minor, dim, major7, minor7, dom7, dim7, hdim7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_chords);
        rootPicker = findViewById(R.id.rootPicker);
        rootPicker.setDisplayedValues(notes);
        rootPicker.setMinValue(0);
        rootPicker.setMaxValue(notes.length - 1);
        rootPicker.setWrapSelectorWheel(true);
        chordPicker = findViewById(R.id.chordPicker);
        chordPicker.setDisplayedValues(chordNames);
        chordPicker.setMinValue(0);
        chordPicker.setMaxValue(chordNames.length - 1);
        chordPicker.setWrapSelectorWheel(false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        double tuning = Double.valueOf(sharedPref.getString("tuning", "441"));
        rootfreqs = calculateRoots(tuning);
    }

    @Override
    public void onPause() {
        super.onPause();
        onStop();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopChord();
    }

    public void playChord(View view) {

        stopChord();
        double rootFrequency = rootfreqs[rootPicker.getValue()];
        double[] ratios = chordRatios[chordPicker.getValue()];

        if (rootPicker.getValue() < 5) {
            rootFrequency *= 2;
        }

        for (double r: ratios) {
            PlaySound note = new PlaySound(rootFrequency * r);
            note.playSound();
            activeNotes.add(note);
            System.out.println(r);
        }
    }

    public void stopChord(View view) {
        stopChord();
    }

    public void stopChord() {
        for (PlaySound p: activeNotes) {
            p.stopSound();
        }
        activeNotes.clear();
    }

    private double[] calculateRoots(double tuning) {
        double[] freqs = new double[12];
        for (int i = -9; i <= 2; i++) {
            freqs[i + 9] = tuning / 2 * Math.pow(HALFSTEP, i);
        }
        return freqs;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chord_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.settings){
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
        }
        if (id == R.id.note_mode) {
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}