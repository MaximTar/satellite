package utils;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Maxim Tarasov on 31.08.2017.
 */
public class WriteUtils {
    public static void write(String fileName, String text) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(text);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
