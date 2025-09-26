# Pitcher 🎵

**Pitcher** is an Android music app that generates continuous drone tones for musical practice, tuning, and harmonic exploration. The app supports both individual note playback and full chord progressions with customizable tuning systems.

## Features

### 🎼 Two Playing Modes
- **Individual Note Mode**: Play sustained drone tones for any of the 12 chromatic notes
- **Chord Mode**: Play full chords including:
  - Major (M)
  - Minor (m)  
  - Diminished (dim)
  - Major 7th (M7)
  - Minor 7th (m7)
  - Dominant 7th (7)
  - Diminished 7th (°7)
  - Half-diminished 7th (ø7)

### 🎛️ Tuning Systems
- **Equal Temperament**: Standard 12-tone equal temperament tuning
- **Just Intonation**: Pure harmonic ratios for more natural-sounding intervals
  - Configurable root note for just intonation scaling
  - Uses mathematical frequency ratios for perfect harmonics

### ⚙️ Customizable Settings
- **Tuning Reference**: Adjust the A4 frequency (default: 441 Hz)
- **Temperament Selection**: Switch between equal temperament and just intonation
- **Just Intonation Root**: Choose the root note when using just intonation

## How It Works

The app generates audio tones using Android's `AudioTrack` API, creating sine wave patterns at specific frequencies. In chord mode, multiple `PlaySound` instances run simultaneously to create harmonic combinations.

### Frequency Calculations
- **Equal Temperament**: Uses the standard formula with semitone ratio of 2^(1/12)
- **Just Intonation**: Uses pure mathematical ratios (e.g., 3:2 for perfect fifth, 5:4 for major third)

## Technical Details

- **Target SDK**: Android API 31
- **Minimum SDK**: Android API 26 (Android 8.0+)
- **Audio Format**: 16-bit PCM, 4000 Hz sample rate
- **Language**: Java
- **Architecture**: Activity-based with preference management

### Key Components
- `MainActivity`: Individual note playback interface
- `ChordsActivity`: Chord selection and playback
- `SettingsActivity`: Tuning and temperament configuration
- `PlaySound`: Audio generation and playback engine
- `ButtonState`: UI state management for note buttons

## Installation

1. Clone the repository
2. Open in Android Studio
3. Build and run on an Android device or emulator (API 26+)

```bash
git clone https://github.com/Noam-Elisha/Pitcher.git
cd Pitcher
```

## Usage

### Individual Notes
1. Launch the app to enter individual note mode
2. Tap any of the 12 chromatic note buttons to play a sustained drone
3. Tap the button again to stop the tone
4. Multiple notes can be played simultaneously

### Chord Mode  
1. Navigate to Chord Mode from the main menu
2. Use the root note picker to select the chord root
3. Use the chord type picker to select the desired chord quality
4. Tap "Play Chord" to hear the full harmonic combination
5. Tap "Stop Chord" to silence all notes

### Settings
1. Access Settings from the menu
2. Adjust the tuning reference frequency (A4)
3. Toggle between Equal Temperament and Just Intonation
4. Select the root note for Just Intonation calculations

## Musical Theory

The app implements both modern and historical tuning approaches:

- **Equal Temperament**: Divides the octave into 12 equal parts, enabling music in any key but with slightly impure intervals
- **Just Intonation**: Uses pure frequency ratios that create perfectly consonant intervals but are key-dependent

This makes Pitcher useful for:
- **Musicians**: Practice with reference tones and explore different temperaments
- **Music Students**: Learn to hear the difference between tuning systems  
- **Composers**: Experiment with pure harmonic relationships
- **Instrument Tuning**: Use as a reference for acoustic instruments

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## License

This project is open source. Please check the repository for license details.

---

*Pitcher - Explore the mathematics and beauty of musical harmony*