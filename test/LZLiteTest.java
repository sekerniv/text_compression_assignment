import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class LZLiteTest {

    private LZLite lzLite;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void T01_testConstructor() {
        int expectedWindowSize = 100;
        LZLite lzlite = new LZLite(expectedWindowSize, true);
        assertEquals(expectedWindowSize, lzlite.getWindowSize(), "Wrong window size");
        assertEquals(0, lzlite.getSlidingWindow().length(), "Initial window should be \"\"");
        assertTrue(lzlite.getTokenizer() instanceof ReadableTokenizer, "The LZLite constructor should initiate a tokenizer of type ReadableTokenizer");
    }

    @Test
    public void T02_testAppendToSlidingWindow() {
        LZLite lzLite = new LZLite(5, true);
        lzLite.appendToSlidingWindow("ABC");
        assertEquals("ABC", lzLite.getSlidingWindow(), "Window (size 5) after appending ABC is incorrect");
        lzLite.appendToSlidingWindow("DE");
        assertEquals("ABCDE", lzLite.getSlidingWindow(), "Window (size 5) after appending ABC and then DE is incorrect");
        lzLite.appendToSlidingWindow("FG");
        assertEquals("CDEFG", lzLite.getSlidingWindow(), "Window (size 5) after appending ABC, then DE, then FG is incorrect");
    }

    @Test
    public void T03_testMaxMatchInSlidingWindow() {
        LZLite lzLite = new LZLite(10, true);

        // Set initial window content with repeated sequences
        lzLite.appendToSlidingWindow("ABABCDEFAB");

        // Test exact match
        assertEquals("CDEF", lzLite.maxMatchInWindow("CDEFGHI", 0),
                "Should find exact longest match 'CDEF' in sliding window (ABABCDEFAB).");

        // Test no match case
        assertEquals("", lzLite.maxMatchInWindow("XYZ", 0),
                "Should return empty match when no prefix matches.");

        // Test partial match case
        assertEquals("ABA", lzLite.maxMatchInWindow("ABAXYZ", 0),
                "Should correctly identify partial match 'ABA' from sliding window(ABABCDEFAB).");

        // Test match that ends at the end of sliding window
        assertEquals("FAB", lzLite.maxMatchInWindow("FABZ", 0),
                "Should correctly identify match 'FAB' at the end of sliding window(ABABCDEFAB).");

        // Test choosing longest possible match over a shorter earlier match
        lzLite.appendToSlidingWindow("XYZXYABCD");
        assertEquals("ABCD", lzLite.maxMatchInWindow("CDABCDX", 2),
                "Should prefer longest match 'ABCD' over shorter earlier matches in window (ABABCDEFAB).");
    }

    @Test
    public void T05_testZip() {
        // Case 1: No repetition (no compression)
        LZLite lzLite1 = new LZLite(20, true);
        String input1 = "abcdefg";
        String expected1 = "abcdefg";
        assertEquals(expected1, lzLite1.zip(input1),
                "Should remain unchanged (no repetitions)");

        // Case 2: Token longer than original (no compression)
        LZLite lzLite2 = new LZLite(20, true);
        String input2 = "ab ab";
        String expected2 = "ab ab";
        assertEquals(expected2, lzLite2.zip(input2),
                "Should NOT compress, token longer than repeated text");

        // Case 3: Token equal length to original text (no compression)
        LZLite lzLite3 = new LZLite(20, true);
        String input3 = "abcd abcd";
        String expected3 = "abcd abcd";
        assertEquals(expected3, lzLite3.zip(input3),
                "Should NOT compress when token length equals original length");

        // Case 4: Beneficial compression considering spaces
        LZLite lzLite4 = new LZLite(20, true);
        String input4 = "hello hello hello";
        String expected4 = "hello ^6,6^hello";
        assertEquals(expected4, lzLite4.zip(input4),
                "Should compress since token (^6,6^) shorter than original repeated text (including spaces)");

        // Case 5: Long beneficial repetition
        LZLite lzLite5 = new LZLite(30, true);
        String input5 = "abcdefghijklmabcdefghijklm";
        String expected5 = "abcdefghijklm^13,13^";
        assertEquals(expected5, lzLite5.zip(input5),
                "Should compress beneficial long repetition correctly");

        // Case 6: Match moves out of window, no compression possible
        LZLite lzLite6 = new LZLite(10, true);
        String input6 = "abcdefghij abc";
        String expected6 = "abcdefghij abc";
        assertEquals(expected6, lzLite6.zip(input6),
                "Should NOT compress, match moved out of window");

        // 7: Complex multiple compressions
        LZLite lzLite7 = new LZLite(30, true);
        String input7 = "the dog chased the cat and the dog caught the cat";
        String expected7 = "the dog chased the cat and ^27,9^aught^27,8^";
        assertEquals(expected7, lzLite7.zip(input7));
    }

    @Test
    public void T06_testZipAndUnzipFileNames() {
        // Test zipFileName with simple file name
        assertEquals("file.lz77.txt",
                LZLite.zipFileName("file.txt"),
                "Should correctly convert simple file name for zip");

        // Test zipFileName with folder path
        assertEquals("test_files/file.lz77.txt",
                LZLite.zipFileName("test_files/file.txt"),
                "Should correctly convert file path with folder for zip");

        // Test zipFileName with incorrect file extension
        assertNull(LZLite.zipFileName("document.pdf"),
                "Should return null for invalid file type in zip");

        // Test unzipFileName with simple compressed file name
        assertEquals("file.decompressed.txt",
                LZLite.unzipFileName("file.lz77.txt"),
                "Should correctly convert simple compressed file name for unzip");

        // Test unzipFileName with folder path
        assertEquals("test_files/file.decompressed.txt",
                LZLite.unzipFileName("test_files/file.lz77.txt"),
                "Should correctly convert compressed file path with folder for unzip");

        // Test unzipFileName with invalid input (wrong suffix)
        assertNull(LZLite.unzipFileName("test_files/file.txt"),
                "Should return null for invalid compressed file name");
    }

    @Test
    public void T08_testUnzip() {
        LZLite lzLite = new LZLite(30, true);

        // Case 1: Simple text without compression
        assertEquals("abcdefg",
                lzLite.unzip("abcdefg"),
                "Should return the original text without changes when no tokens exist.");

        // Case 2: Single simple compression token
        assertEquals("hello hello ",
                lzLite.unzip("hello ^6,6^"),
                "Should correctly unzip a simple repeated sequence.");

        // Case 3: Multiple tokens
        assertEquals("abc abc abc",
                lzLite.unzip("abc ^4,4^^8,3^"),
                "Should correctly unzip multiple repeated sequences.");

        // Case 4: Longer sequence with tokens
        String compressed = "the dog chased ^15,8^and caught^19,8^";
        String original = "the dog chased the dog and caught the dog";
        assertEquals(original,
                lzLite.unzip(compressed),
                "Should correctly unzip complex sequence with multiple tokens.");

        // Case 5: Edge case with large-length token
        assertEquals("abcdefghijklmabcdefghijklm",
                lzLite.unzip("abcdefghijklm^13,13^"),
                "Should correctly unzip longer sequence with large-length token.");

        // Case 6: Multiple tokens with spaces and special chars
        compressed = "good night! ^12,12^again!";
        original = "good night! good night! again!";
        assertEquals(original,
                lzLite.unzip(compressed),
                "Should correctly unzip text with spaces and special characters.");
    }

    @Test
    public void T11_testUnzipWithLeanTokenizer() {
        LZLite lzLite = new LZLite(30, false);
        String compressed = lzLite.unzip("ABC D^" + (char) 4 + (char) 2);
        assertEquals("ABC DBC",
                lzLite.unzip(compressed), "Trying to compress \"ABC D^\" + (char) 4 + (char) 2");

        compressed = lzLite.unzip("ABC D^" + (char) 1 + (char) 1);
        assertEquals("ABC DD",
                lzLite.unzip(compressed),
                "Trying to compress \"ABC D^\" + (char) 1 + (char) 1");

    }

}
