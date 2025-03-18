/**
 * A tokenizer for encoding and decoding LZ77-style compression tokens in a readable format.
 * Tokens are represented as: "^distance,length^".
 */
public class ReadableTokenizer implements Tokenizer {

    //TODO: TASK 4
    public String toTokenString(int distance, int length) {
        return "^" + distance + "," + length + "^";
    }

    // TODO TASK 4
    public int[] fromTokenString(String tokenText, int index) {
        int endIndex = tokenText.indexOf("^", index + 1);
        int commaPosition = tokenText.indexOf(",", index);
        String part1 = tokenText.substring(index + 1, commaPosition);
        String part2 = tokenText.substring(commaPosition + 1, endIndex);
        int backwardDistance = Integer.parseInt(part1);
        int length = Integer.parseInt(part2);
        int numOfChars = endIndex - index + 1;
        int[] ret = {backwardDistance, length, numOfChars};
        return ret;
    }
}