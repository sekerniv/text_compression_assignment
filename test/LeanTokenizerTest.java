import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LeanTokenizerTest {

    @Test
    public void testToTokenString() {
        LeanTokenizer tokenizer = new LeanTokenizer();

        // Regular case: small numbers
        String token1 = tokenizer.toTokenString(10, 5);
        assertEquals("^" + (char) 10 + (char) 5, token1, "Encoding (10,5) failed");

        // Large values within char range
        String token2 = tokenizer.toTokenString(300, 150);
        assertEquals("^" + (char) 300 + (char) 150, token2, "Encoding (300,150) failed");

        // Edge case: Maximum value 65535
        String token3 = tokenizer.toTokenString(65535, 255);
        assertEquals("^" + (char) 65535 + (char) 255, token3, "Encoding max values failed");
    }

    @Test
    public void testFromTokenString() {
        LeanTokenizer tokenizer = new LeanTokenizer();

        // Decoding regular cases
        int[] decoded1 = tokenizer.fromTokenString("ABC D^" + (char) 10 + (char) 5, 5);
        assertArrayEquals(new int[]{10, 5, 3}, decoded1, "Decoding (10,5) failed");

        int[] decoded2 = tokenizer.fromTokenString("D^" + (char) 300 + (char) 150, 1);
        assertArrayEquals(new int[]{300, 150, 3}, decoded2, "Decoding (300,150) failed");

        // Edge case: Maximum possible values
        int[] decoded3 = tokenizer.fromTokenString("^" + (char) 65535 + (char) 255, 0);
        assertArrayEquals(new int[]{65535, 255, 3}, decoded3, "Decoding max values failed");

        // Minimum values
        int[] decoded4 = tokenizer.fromTokenString("A^AB^&1D^" + (char) 65 + (char) 67, 8);
        assertArrayEquals(new int[]{65, 67, 3}, decoded4, "Decoding (0,0) failed");
    }

    @Test
    public void testTokenRoundTrip() {
        LeanTokenizer tokenizer = new LeanTokenizer();

        int backwardDistance = 500;
        int length = 50;

        // Encode
        String encoded = tokenizer.toTokenString(backwardDistance, length);
        assertNotNull(encoded, "Encoded token should not be null");

        // Decode
        int[] decoded = tokenizer.fromTokenString(encoded, 0);
        assertArrayEquals(new int[]{backwardDistance, length, 3}, decoded, "Round-trip encoding/decoding failed");
    }

    @Test
    public void testInvalidTokenHandling() {
        LeanTokenizer tokenizer = new LeanTokenizer();

        // Invalid input: Empty token
        assertThrows(StringIndexOutOfBoundsException.class, () -> tokenizer.fromTokenString("", 0));

        // Invalid input: Token without enough characters
        assertThrows(StringIndexOutOfBoundsException.class, () -> tokenizer.fromTokenString("^", 0));

        assertThrows(StringIndexOutOfBoundsException.class, () -> tokenizer.fromTokenString("^A", 0));
    }
}
