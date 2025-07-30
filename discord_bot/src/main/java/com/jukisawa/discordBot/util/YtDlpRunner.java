package com.jukisawa.discordBot.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class YtDlpRunner {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(YtDlpRunner.class);

    private static Path extractYtDlpBinary() throws IOException {
        String binaryName = System.getProperty("os.name").toLowerCase().contains("win") ? "yt-dlp.exe" : "yt-dlp";
        InputStream in = YtDlpRunner.class.getResourceAsStream("/yt-dlp/" + binaryName);
        if (in == null)
            throw new FileNotFoundException("yt-dlp binary not found in resources");

        Path tempFile = Files.createTempFile("yt-dlp-", binaryName);
        tempFile.toFile().deleteOnExit();

        Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);

        if (!System.getProperty("os.name").toLowerCase().contains("win")) {
            tempFile.toFile().setExecutable(true);
        }

        return tempFile;
    }

    public static String runYtDlp(String... args) throws IOException, InterruptedException {
        Path ytDlpPath = extractYtDlpBinary();
        List<String> command = new ArrayList<>();
        command.add(ytDlpPath.toAbsolutePath().toString());
        command.addAll(List.of(args));

        ProcessBuilder pb = new ProcessBuilder(command);
        System.out.println(command);

        Process process = pb.start();

        // Read output
        try (BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

            StringBuilder output = new StringBuilder();
            String line;

            // Read stdout
            while ((line = stdOut.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Read and discard stderr or log it somewhere else
            while ((line = stdErr.readLine()) != null) {
                // Optionally log warnings or ignore
                logger.error("[yt-dlp warning] " + line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("yt-dlp failed with code: " + exitCode);
            }
            return output.toString().trim();
        }
    }
}