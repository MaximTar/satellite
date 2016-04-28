package main.java.utils;

import java.io.FileWriter;
import java.io.IOException;

public class WritingUtils {
    public static void write(String fileName, String text) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(text);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
