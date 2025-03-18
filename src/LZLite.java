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
        this.windowSize = windowSize;
        this.slidingWindow = "";
        if (readable)
            this.tokenizer = new ReadableTokenizer();
        else
            this.tokenizer = new LeanTokenizer();
    }

    //TODO: TASK 2
    public void appendToSlidingWindow(String st) {
        slidingWindow += st;
        if (slidingWindow.length() > windowSize) {
            slidingWindow = slidingWindow.substring(slidingWindow.length() - windowSize);
        }
    }

    //TODO: TASK 3
    public String maxMatchInWindow(String input, int pos) {
        String match = "";
        while (pos < input.length()) {
            char ch = input.charAt(pos);
            if (slidingWindow.indexOf(match + ch) == -1) {
                return match;
            }
            match += ch;
            pos++;
        }
        return match;
    }

    //TODO: TASK 5
    public String zip(String input) {
        String output = "";

        int loc = 0;
        while (loc < input.length()) {
            String maxMatch = maxMatchInWindow(input, loc);

            if (maxMatch.length() > 0) {
                int matchIdx = slidingWindow.indexOf(maxMatch);
                int backwardDistance = slidingWindow.length() - matchIdx;
                if (maxMatch.length() > backwardDistance) {
                    System.out.println("maxMatch.length() > backwardDistance");
                    System.out.println(matchIdx);
                }

                String codedString = this.tokenizer.toTokenString(backwardDistance, maxMatch.length());
                if (codedString.length() < maxMatch.length()) {
                    if (backwardDistance == 53 && maxMatch.length() == 126) {
                        System.out.println("Found one! " + maxMatch);
                    }
                    output += codedString;
                    appendToSlidingWindow(maxMatch);
                    loc += maxMatch.length();
                } else {
                    output += input.charAt(loc);
                    appendToSlidingWindow(input.charAt(loc) + "");
                    loc += 1;
                }
            } else {
                output += input.charAt(loc);
                appendToSlidingWindow(input.charAt(loc) + "");
                loc += 1;
            }
        }
        return output;
    }

    //TODO: TASK 6
    public static String zipFileName(String fileName) {
        if (!fileName.endsWith(".txt")) {
            System.err.println("Error: wrong file type");
            return null;
        }
        return fileName.replace(".txt", ".lz77.txt");
    }

    //TODO: TASK 6
    public static String unzipFileName(String fileName) {
        if (!fileName.endsWith(".lz77.txt")) {
            System.err.println("Error: File does not appear to be compressed.");
            return null;
        }
        return fileName.replace(".lz77.txt", ".decompressed.txt");
    }

    //TODO: TASK 7
    public static String zipFile(String file, int windowSize, boolean readable) {

        LZLite LZLite = new LZLite(windowSize, readable);
        String content = FileUtils.readFile(file);
        String compressedFileName = zipFileName(file);
        if (compressedFileName == null) return null;
        String compressed = LZLite.zip(content);
        FileUtils.writeFile(compressedFileName, compressed);
        return compressedFileName;
    }

    //TODO: TASK 8
    public String unzip(String input) {
        String output = "";

        for (int i = 0; i < input.length(); ) {
            if (input.charAt(i) == '^') {
                int[] token = this.tokenizer.fromTokenString(input, i);
                int start = output.length() - token[0];
                if (start >= 0 && token[1] > 0) {
                    String referencedText = output.substring(start, start + token[1]);
                    output += referencedText;
                }
                i += token[2];
            } else {
                char nextChar = input.charAt(i);
                i++;
                output += nextChar;
            }
        }
        return output;
    }

    //TODO: TASK 9
    public static String unzipFile(String file, int windowSize, boolean readable) {
        LZLite LZLite = new LZLite(windowSize, readable);
        String zippedContent = FileUtils.readFile(file);
        String unzipFileName = unzipFileName(file);
        if (unzipFileName == null) return null;
        String unzippedContent = LZLite.unzip(zippedContent);
        FileUtils.writeFile(unzipFileName, unzippedContent);
        return unzipFileName;
    }

    //TODO: TASK 9
    public static void main(String[] args) {
        String zipFileName = zipFile("test_files/genesis.txt",
                MAX_WINDOW_SIZE, true);
        String unzipFile = unzipFile(zipFileName,
                MAX_WINDOW_SIZE, true);
        System.out.println("Unzip to file " + unzipFile + " completed!");
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
