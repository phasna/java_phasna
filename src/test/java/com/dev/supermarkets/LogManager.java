package com.dev.supermarkets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogManager {
    private static final String LOG_FILE = "src/test/java/com/dev/supermarkets/log.txt";

    public static void log(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[DATE: " + timestamp + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier de log : " + e.getMessage());
        }
    }
}