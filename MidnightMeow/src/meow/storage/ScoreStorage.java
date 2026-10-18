/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meow.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author niki
 */
public class ScoreStorage {

    private static final Path FILE_PATH = Path.of("data", "best-score.txt");

    public static int loadBestScore() {
        try {
            if (Files.notExists(FILE_PATH)) {
                saveBestScore(0);
                return 0;
            }

            String content = Files.readString(FILE_PATH).trim();
            return Integer.parseInt(content);
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    public static void saveBestScore(int score) {
        try {
            Files.createDirectories(FILE_PATH.getParent());
            Files.writeString(FILE_PATH, String.valueOf(score));
        } catch (IOException e) {
            System.err.println(
                    "Could not save best score: " + e.getMessage()
            );
        }
    }

}
