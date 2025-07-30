package com.jukisawa.discordBot.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.SynthesisException;

public class TTSUtil {
    private MaryInterface marytts;

    public TTSUtil() {
        try {
            marytts = new LocalMaryInterface();
            marytts.setVoice("cmu-slt-hsmm"); // Set default voice
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File generateTTS(String text) throws IOException, SynthesisException {
        AudioInputStream audio = marytts.generateAudio(text);

        File ttsFile = new File(System.getProperty("java.io.tmpdir"),
                "tts-" + UUID.randomUUID() + ".wav");

        AudioSystem.write(audio, javax.sound.sampled.AudioFileFormat.Type.WAVE, ttsFile);
        return ttsFile;
    }
}