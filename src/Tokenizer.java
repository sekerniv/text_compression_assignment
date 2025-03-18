/**
 * An interface for encoding and decoding LZ77-style compression tokens.
 * Implementations of this interface define how tokens are represented in the compressed text.
 */
public interface Tokenizer {

    /**
     * Extracts a compression token from a given text at the specified index.
     *
     * @param tokenText The compressed text containing the token.
     * @param index     The position in the text where the token starts.
     * @return An array containing:
     *         - backward distance (int): how far back in the text the match starts.
     *         - length (int): the number of characters in the repeated sequence.
     *         - number of characters used to represent the token (int).
     */
    int[] fromTokenString(String tokenText, int index);

    /**
     * Encodes a compression token into a string representation.
     *
     * @param distance The backward distance in the sliding window.
     * @param length   The length of the repeated sequence.
     * @return A string representation of the token.
     */
    String toTokenString(int distance, int length);
}
