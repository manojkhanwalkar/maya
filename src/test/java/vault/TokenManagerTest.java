package vault;

import data.Token;
import org.junit.Test;
import util.CardUtil;

import java.util.Optional;

import static org.junit.Assert.*;

public class TokenManagerTest {

    @Test
    public void roundTripsEveryTokenType() {
        TokenManager tm = new TokenManager();
        String pan = CardUtil.createVCard();
        for (Token.Type type : Token.Type.values()) {
            Token token = tm.put("m1", pan, type).orElseThrow();
            assertEquals(Optional.of(pan), tm.get("m1", token));
        }
    }

    @Test
    public void rejectsWrongMerchantWrongTypeAndInvalidPan() {
        TokenManager tm = new TokenManager();
        String pan = CardUtil.createVCard();
        Token token = tm.put("m1", pan, Token.Type.FPE1).orElseThrow();

        assertEquals(Optional.empty(), tm.get("m2", token));

        Token wrongType = new Token();
        wrongType.setType(Token.Type.Hash);
        wrongType.setValue(token.getValue());
        assertEquals(Optional.empty(), tm.get("m1", wrongType));

        assertEquals(Optional.empty(), tm.put("m1", "abc", Token.Type.FPE));
    }
}
