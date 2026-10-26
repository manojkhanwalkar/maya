package util;

import com.nimbusds.jose.jwk.JWKSet;
import data.KeyExchange;

import java.text.ParseException;

public class KeyExchangeUtil {

    static final int MAX_SENDER_KEYS = 20;

    public static KeyExchange keyExchange(KeyExchange request, ClientCache clientCache) {


        try {
            if (request == null || request.getJavawebKeySet() == null)
                return null;

            JWKSet senderKeys = JWKSet.parse(request.getJavawebKeySet());
            int keyCount = senderKeys.getKeys().size();
            // Reject oversized key sets, reused key ids (which would let one caller take over
            // another client's key id) and registrations once the cache is full.
            if (keyCount == 0 || keyCount > MAX_SENDER_KEYS || clientCache.containsAny(senderKeys)
                    || !clientCache.hasCapacityFor(keyCount + 20)) {
                return null;
            }

            ClientHandler clientHandler = new ClientHandler();
            clientHandler.init(request.getJavawebKeySet());


            KeyExchange response = new KeyExchange();
            response.setJavawebKeySet(clientHandler.getKAandSignPubKeys());

            clientCache.put(senderKeys,clientHandler);
            clientCache.put(JWKSet.parse(response.getJavawebKeySet()),clientHandler);

            return response;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
