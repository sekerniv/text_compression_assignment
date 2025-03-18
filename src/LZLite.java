/**
 * Class for performing LZ77 compression/decompression.
 */


/**
 * Class for performing compression/decompression loosely based on LZ77.
 */
public class LZLite {
    public static int MAX_WINDOW_SIZE = 65535;
    private int windowSize;
    private String slidingWindow;
    private Tokenizer tokenizer;

    //TODO: TASK 1
    public LZLite(int windowSize, boolean readable) {

    }

    //TODO: TASK 2
    public void appendToSlidingWindow(String st) {

    }

    //TODO: TASK 3
    public String maxMatchInWindow(String input, int pos) {

        return null;
    }

    //TODO: TASK 5
    public String zip(String input) {

        return null;
    }

    //TODO: TASK 6
    public static String zipFileName(String fileName) {

        return null;
    }

    //TODO: TASK 6
    public static String unzipFileName(String fileName) {

        return null;
    }

    //TODO: TASK 7
    public static String zipFile(String file, int windowSize, boolean readable) {

        return null;
    }

    //TODO: TASK 8
    public String unzip(String input) {

        return null;
    }

    //TODO: TASK 9
    public static String unzipFile(String file, int windowSize, boolean readable) {

        return null;
    }

    //TODO: TASK 9
    public static void main(String[] args) {

    }


    // DON'T DELETE THE GETTERS! THEY ARE REQUIRED FOR TESTING
    public int getWindowSize() {
        return windowSize;
    }

    public String getSlidingWindow() {
        return slidingWindow;
    }

    public Tokenizer getTokenizer() {
        return tokenizer;
    }
}
