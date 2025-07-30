package com.jukisawa.discordBot.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TTSPhraseService {

    private Map<String, List<String>> gamePhrases = new HashMap<>();
    private Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(TTSPhraseService.class);

    public TTSPhraseService() {
        try {
            loadPhrases();
        } catch (IOException e) {
            logger.error("Error loading TTS phrases", e);
        }
    }

    public void loadPhrases() throws IOException {
        InputStream is = getClass().getResourceAsStream("/ttsStorage.json");

        if (is == null)
            throw new RuntimeException("File not found!");

        // Read file to String
        String jsonText = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(jsonText);

        Map<String, List<String>> phrasesMap = new HashMap<>();

        for (String gameName : json.keySet()) {
            JSONArray arr = json.getJSONArray(gameName);
            List<String> phrases = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                phrases.add(arr.getString(i));
            }
            phrasesMap.put(gameName, phrases);
        }

        gamePhrases = phrasesMap;
    }

    public String getRandomPhraseForGame(String gameName) {
        List<String> phrases = gamePhrases.get(gameName.toLowerCase());
        if (phrases == null || phrases.isEmpty()) {
            return "No phrases found for game: " + gameName;
        }
        return phrases.get(random.nextInt(phrases.size()));
    }

    public boolean hasPhrasesForGame(String gameName) {
        return gamePhrases.containsKey(gameName.toLowerCase()) && !gamePhrases.get(gameName.toLowerCase()).isEmpty();
    }
}
