package com.jukisawa.discordBot.audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.jukisawa.discordBot.dto.music.Song;

public class PlaylistManager {

    private static final PlaylistManager INSTANCE = new PlaylistManager();
    private final Map<Long, Queue<Song>> currentPlaylist = new HashMap<>();
    private final Map<Long, List<Song>> originalPlaylist = new HashMap<>();
    private Map<Long, Boolean> repeatQueue = new HashMap<>();

    public static PlaylistManager getInstance() {
        return INSTANCE;
    }

    public Queue<Song> getPlaylist(long guildId) {
        return currentPlaylist.computeIfAbsent(guildId, id -> new LinkedList<>());
    }

    public void addSong(long guildId, Song song) {
        currentPlaylist.computeIfAbsent(guildId, k -> new LinkedList<>()).add(song);
        originalPlaylist.computeIfAbsent(guildId, k -> new ArrayList<>()).add(song);
    }

    public Song removeSong(long guildId, String title) {
        Queue<Song> playlist = getPlaylist(guildId);
        for (Song song : playlist) {
            if (song.getTitle().equalsIgnoreCase(title)) {
                playlist.remove(song);
                return song;
            }
        }
        return null;
    }

    public void clearPlaylist(long guildId) {
        getPlaylist(guildId).clear();
    }

    public void shufflePlaylist(long guildId) {
        List<Song> list = new ArrayList<>(getPlaylist(guildId));
        Collections.shuffle(list);
        currentPlaylist.put(guildId, new LinkedList<>(list));
    }

    public Song nextSong(long guildId) {
        return getPlaylist(guildId).poll();
    }

    public void switchRepeatQueue(Long guildId) {
        repeatQueue.put(guildId, !repeatQueue.getOrDefault(guildId, false));
    }

    public boolean isRepeatQueue(Long guildId) {
        return repeatQueue.getOrDefault(guildId, false);
    }

    public void resetQueueFromOriginal(long guildId) {
        List<Song> original = originalPlaylist.get(guildId);
        if (original != null && !original.isEmpty()) {
            Queue<Song> repeated = new LinkedList<>(original);
            currentPlaylist.put(guildId, repeated);
        }
    }
}
