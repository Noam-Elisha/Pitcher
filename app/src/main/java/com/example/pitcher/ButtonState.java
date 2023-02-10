package com.example.pitcher;

public class ButtonState {
    double frequency;
    boolean isOn;
    PlaySound player;
    public ButtonState(double freq) {
        this.frequency = freq;
        this.isOn = false;
    }
}
