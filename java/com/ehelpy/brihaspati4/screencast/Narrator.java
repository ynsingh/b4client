package com.ehelpy.brihaspati4.screencast;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
// lt col jitesh ps updated this on 14 june 2020 ; 0500 Hrs
//this code is for narrator a simple and common class for helping visually disabled
//class commonly created which can br called by any module

public class Narrator {
    public static void read (String s) {
        Voice voice;
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        voice = VoiceManager.getInstance().getVoice("kevin");
        if (voice != null) voice.allocate();
        try {
            voice.setRate(100);
            voice.setPitch(150);
            voice.setVolume(4);
            voice.speak(s);
        } catch (Exception e) { e.printStackTrace(); }
    }
}





