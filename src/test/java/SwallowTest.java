import org.junit.Test;

import com.wolfram.alpha.WAException;

import static org.junit.Assert.assertEquals;

public class SwallowTest {
    @Test
    public void testSwallow() throws WAException {
        assertEquals(25, new Swallow().determineSpeedMph());
    }
}
