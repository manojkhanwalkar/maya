package ccvid;

import org.junit.Test;

import javax.ws.rs.BadRequestException;

import static org.junit.Assert.assertEquals;

public class NonceManagerTest {

    @Test
    public void returnsRequestedNonces() {
        assertEquals(10, new NonceManager().getNonces(10).size());
    }

    @Test(expected = BadRequestException.class)
    public void rejectsHugeRequests() {
        new NonceManager().getNonces(Integer.MAX_VALUE);
    }

    @Test(expected = BadRequestException.class)
    public void rejectsNegativeRequests() {
        new NonceManager().getNonces(-1);
    }
}
