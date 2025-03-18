import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

public class LZLiteIntegrationTest {

    private static final String ORIGINAL_FILE = "test_files/genesis.txt";
    private File tempOriginalFile;
    private File compressedFile;
    private File decompressedFile;

    @BeforeEach
    public void setUp() throws IOException {
        // Create a temporary copy of genesis.txt with a unique name
        tempOriginalFile = new File("test_files/genesis_temp_152939393.txt");
        Files.copy(new File(ORIGINAL_FILE).toPath(), tempOriginalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void testZipAndUnzipFile_LeanTokenizer() {
        runZipUnzipTest(false);
    }

    @Test
    public void testZipAndUnzipFile_ReadableTokenizer() {
        runZipUnzipTest(true);
    }

    private void runZipUnzipTest(boolean readable) {
        assertTrue(tempOriginalFile.exists(), "Temporary original file should exist before zipping");

        // Compress the temporary file
        String zipFilePath = LZLite.zipFile(tempOriginalFile.getPath(), LZLite.MAX_WINDOW_SIZE, readable);
        compressedFile = new File(zipFilePath);
        assertTrue(compressedFile.exists(), "Compressed file should be created");

        // Decompress the file
        String unzipFilePath = LZLite.unzipFile(zipFilePath, LZLite.MAX_WINDOW_SIZE, readable);
        decompressedFile = new File(unzipFilePath);
        assertTrue(decompressedFile.exists(), "Decompressed file should be created");

        // Compare the contents of the original and decompressed files
        try {
            String originalContent = Files.readString(tempOriginalFile.toPath());
            String decompressedContent = Files.readString(decompressedFile.toPath());

            assertEquals(originalContent, decompressedContent,
                    "Decompressed content does not match original. Try testing zipping and unzipping yourself and compare the content of the files");

            // Final check to ensure all files exist before deletion
            assertTrue(tempOriginalFile.exists(), "Original temp file should still exist before cleanup");
            assertTrue(compressedFile.exists(), "Compressed file should still exist before cleanup");
            assertTrue(decompressedFile.exists(), "Decompressed file should still exist before cleanup");

        } catch (IOException e) {
            fail("Error reading file contents: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        // Delete all temporary files
        if (tempOriginalFile != null && tempOriginalFile.exists()) {
            tempOriginalFile.delete();
        }
        if (compressedFile != null && compressedFile.exists()) {
            compressedFile.delete();
        }
        if (decompressedFile != null && decompressedFile.exists()) {
            decompressedFile.delete();
        }
    }
}
