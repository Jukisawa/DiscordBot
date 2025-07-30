package com.jukisawa.discordBot.service.musik;

import java.util.HashMap;
import java.util.Map;

import com.jukisawa.discordBot.util.YtDlpRunner;

public class CachingSongService implements SongService {

    private final Map<String, String> titleToUrlCache = new HashMap<>();
    private final Map<String, String> urlToTitleCache = new HashMap<>();

    @Override
    public String getUrlFromTitle(String title) throws Exception {
        if (titleToUrlCache.containsKey(title)) {
            return titleToUrlCache.get(title);
        }

        // Try YouTube first
        String url = YtDlpRunner.runYtDlp(
                "--default-search", "ytsearch1",
                "--quiet", "--no-warnings", "--no-playlist", "--flat-playlist",
                "--skip-download", "--print", "url",
                title);

        if (url == null || url.isBlank()) {
            // If YouTube failed, try SoundCloud
            url = YtDlpRunner.runYtDlp(
                    "--default-search", "scsearch1",
                    "--skip-download",
                    "--print", "url",
                    title);
        }

        titleToUrlCache.put(title, url);
        urlToTitleCache.put(url, title); // Pre-fill reverse lookup

        return url;
    }

    @Override
    public String getTitleFromUrl(String url) throws Exception {
        if (urlToTitleCache.containsKey(url)) {
            return urlToTitleCache.get(url);
        }

        // Use YtDlpRunner instead of raw ProcessBuilder
        String title = YtDlpRunner.runYtDlp(
                "--skip-download",
                "--print", "%(title)s",
                url).trim();

        if (title.isEmpty()) {
            throw new RuntimeException("Could not extract title from URL: " + url);
        }

        urlToTitleCache.put(url, title);
        titleToUrlCache.put(title, url);

        return title;
    }
}
