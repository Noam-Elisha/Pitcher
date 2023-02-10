package com.example.pitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    HashMap<Pair<String, Integer>, ButtonState> button_notes = new HashMap<>();
    String[] notes_verbose = {"C", "CSHARP", "D", "DSHARP", "E", "F", "FSHARP", "G", "GSHARP", "A", "ASHARP", "B"};
    String[] notes = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    HashMap<String, String> notesToNotesVerbose = new HashMap<>();
    final double HALFSTEP = Math.pow(2, (double) 1/12);
    final double[] RATIOS = {1.0, 16.0/15.0, 9.0/8.0, 6.0/5.0, 5.0/4.0, 4.0/3.0, 45.0/32.0, 3.0/2.0, 8.0/5.0, 5.0/3.0, 9.0/5.0, 15.0/8.0};
    int octave = 4;
    ArrayList<Button> activeButtons = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        for (int i = 0; i < notes.length; i++) {
            notesToNotesVerbose.put(notes[i], notes_verbose[i]);
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean equalTemperament = sharedPref.getBoolean("equal", true);
        String stuning = sharedPref.getString("tuning", "441");
        double tuning = Double.valueOf(stuning);
        if (equalTemperament) {
            initEqualTemperament(tuning);
            Toast.makeText(this, "Tuning: A = " + Double.toString(tuning), Toast.LENGTH_SHORT).show();
        } else {
            String scale = sharedPref.getString("scale", "C");
            initJustIntonation(tuning, scale);
            Toast.makeText(this, "Tuning: A = " + Double.toString(tuning) + " " + "Scale: " + scale, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onStop();
    }

    @Override
    public void onStop() {
        super.onStop();
        for (Button b: activeButtons) {
            playSound(b);
        }
    }

    private void initEqualTemperament(double tuning) {
        for (int i = -3; i <= 3; i++) {
            for (int j = -9; j <= 2; j++) {
                double freq = tuning * Math.pow(HALFSTEP, j);
                button_notes.put(new Pair(notes_verbose[j + 9], 4 + i), new ButtonState(freq * Math.pow(2, i)));
            }
        }
    }

    private void initJustIntonation(double tuning, String scale) {
        List<String> notesList = Arrays.asList(notes.clone());
        double startNoteFreq = tuning * Math.pow(HALFSTEP, notesList.indexOf(scale) - notesList.indexOf("A"));
        int startNote = notesList.indexOf(scale);
        for (int i = 0; i < 12; i += 1) {
            int purej = startNote + i;
            int j = purej % 12;
            double multiplier = 1;
            if (purej != j) {
                multiplier = 0.5;
            }
            double freq = startNoteFreq * RATIOS[i] * multiplier;
            for (int k = -3; k < 3; k++) {
                button_notes.put(new Pair(notes_verbose[j], 4 + k), new ButtonState(freq * Math.pow(2, k)));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.settings){
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
        }
        if (id == R.id.chord_mode) {
            Intent myIntent = new Intent(this, ChordsActivity.class);
            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void playSound(View view) {
        Button b = (Button) view;
        String noteName = b.getText().toString();
        noteName = notesToNotesVerbose.get(noteName);
        ButtonState bstate = button_notes.get(new Pair(noteName, octave));
        if (bstate.isOn) {
            view.setAlpha(1);
            bstate.player.stopSound();
            bstate.isOn = false;
            bstate.player = null;
            b.setTextColor(0xFFFFFFFF);
            activeButtons.remove(b);
        } else {
            b.setTextColor(0xFF23F11C);
            bstate.player = new PlaySound(bstate.frequency);
            bstate.player.playSound();
            bstate.isOn = true;
            activeButtons.add(b);
        }

    }

    public void OctaveUp(View view) {
        if (octave == 6) {
            return;
        }
        octave += 1;
        updateOctave();
    }

    public void OctaveDown(View view) {
        if (octave == 1) {
            return;
        }
        octave -= 1;
        updateOctave();
    }

    private void updateOctave() {
        View octaveText = findViewById(R.id.octaveText);
        TextView text = (TextView) octaveText;
        text.setText("Octave" +  Integer.toString(octave));
        View linearLayout = findViewById(R.id.linearLayout);
        for (int i = 0; i < ((ViewGroup) linearLayout).getChildCount(); i++) {
            View nextChild = ((ViewGroup) linearLayout).getChildAt(i);
            if (!nextChild.isClickable()) {
                continue;
            }
            String noteName = ((Button) nextChild).getText().toString();
            noteName = notesToNotesVerbose.getOrDefault(noteName, null);
            if (noteName == null) {
                continue;
            }
            ButtonState bstate = button_notes.get(new Pair(noteName, octave));
            if (bstate.isOn) {
                ((Button) nextChild).setTextColor(0xFF23F11C);
            } else {
                ((Button) nextChild).setTextColor(0xFFFFFFFF);
            }
        }
    }
}