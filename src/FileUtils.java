import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A utility class for reading from and writing to files.
 * Provides simple methods to handle text file operations.
 */
public class FileUtils {

    /**
     * Reads the entire content of a file and returns it as a string.
     *
     * @param fileName The path to the file to be read.
     * @return The content of the file as a string, or null if an error occurs.
     */
    public static String readFile(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
            return null;
        }
    }

    /**
     * Writes a string to a file, replacing its existing content.
     *
     * @param fileName The path to the file to be written.
     * @param content  The content to write to the file.
     */
    public static void writeFile(String fileName, String content) {
        try {
            Files.write(Paths.get(fileName), content.getBytes());
        } catch (IOException e) {
            System.err.println("Error writing file: " + fileName);
        }
    }
}
