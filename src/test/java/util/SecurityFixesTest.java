package util;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.nimbusds.jose.jwk.JWKSet;
import data.KeyExchange;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SecurityFixesTest {

    public static class Holder {
        public Map<String, Object> extra = new HashMap<>();

        @JsonAnySetter
        public void set(String k, Object v) { extra.put(k, v); }
    }

    @Test
    public void jsonDoesNotHonourAttackerSuppliedTypeIds() {
        // With default typing on, ["java.lang.Thread", {...}] would make Jackson instantiate that class.
        Holder h = (Holder) JSONUtil.fromJSON("{\"x\":[\"java.util.concurrent.atomic.AtomicInteger\",5]}", Holder.class);
        assertNotNull(h);
        assertTrue(h.extra.get("x") instanceof java.util.List);
        assertEquals("java.util.concurrent.atomic.AtomicInteger", ((java.util.List<?>) h.extra.get("x")).get(0));
    }

    @Test
    public void validateCardRejectsNonPans() {
        String pan = CardUtil.createVCard();
        assertTrue(CardUtil.validateCard(pan));
        assertFalse(CardUtil.validateCard(null));
        assertFalse(CardUtil.validateCard(""));
        assertFalse(CardUtil.validateCard("12345"));
        assertFalse(CardUtil.validateCard("4111-1111-1111-1111"));
        assertFalse(CardUtil.validateCard("41111111111111111111111"));
    }

    @Test
    public void keyExchangeRejectsReusedKeyIds() {
        ClientCache cache = new ClientCache();
        Sender sender = new Sender("https://unused/");
        KeyExchange req = new KeyExchange();
        req.setJavawebKeySet(sender.getKAandSignPubKeys());

        assertNotNull(KeyExchangeUtil.keyExchange(req, cache));
        // a second registration with the same key ids must not take them over
        assertNull(KeyExchangeUtil.keyExchange(req, cache));
    }

    @Test
    public void keyExchangeRejectsOversizedKeySets() throws Exception {
        ClientCache cache = new ClientCache();
        Sender a = new Sender("https://unused/");
        Sender b = new Sender("https://unused/");
        JWKSet merged = new JWKSet(new java.util.ArrayList<>() {{
            addAll(JWKSet.parse(a.getKAandSignPubKeys()).getKeys());
            addAll(JWKSet.parse(b.getKAandSignPubKeys()).getKeys());
        }});
        KeyExchange req = new KeyExchange();
        req.setJavawebKeySet(merged.toString());
        assertNull(KeyExchangeUtil.keyExchange(req, cache));
    }
}
