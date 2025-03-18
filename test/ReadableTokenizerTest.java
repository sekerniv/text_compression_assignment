import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ReadableTokenizerTest {

    @Test
    public void testToTokenString() {
        Tokenizer tokenizer = new ReadableTokenizer();
        assertEquals("^1,2^", tokenizer.toTokenString(1, 2));
        assertEquals("^123,54^", tokenizer.toTokenString(123, 54));
        assertEquals("^123,5^", tokenizer.toTokenString(123, 5));
        assertEquals("^543,521^", tokenizer.toTokenString(543, 521));
    }

    @Test
    public void testFromTokenString() {
        ReadableTokenizer tokenizer = new ReadableTokenizer();

        // Basic extraction within bounds
        assertArrayEquals(new int[]{5, 3, 5},
                tokenizer.fromTokenString("Hello^5,3^World", 5),
                "Extracting token ^5,3^ from middle of string failed.");

        // Token at the start (unavoidable edge case)
        assertArrayEquals(new int[]{18, 7, 6},
                tokenizer.fromTokenString("^18,7^StartOfString", 0),
                "Extracting token ^18,7^ at start of string failed.");

        // Token at end of string
        assertArrayEquals(new int[]{4, 2, 5},
                tokenizer.fromTokenString("EndOfString^4,2^", 11),
                "Extracting token ^4,2^ at end of string failed.");

        // Single-digit short token within bounds
        assertArrayEquals(new int[]{2, 1, 5},
                tokenizer.fromTokenString("Go^2,1^!", 2),
                "Extracting short token ^2,1^ failed.");


        // Token with length greater than 10
        assertArrayEquals(new int[]{15, 12, 7},
                tokenizer.fromTokenString("ThisIsALongText^15,12^AfterToken^8,3^ last word", 15),
                "Extracting token ^15,12^ with length > 10 failed.");
    }


}
