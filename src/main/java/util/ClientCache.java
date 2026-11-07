package util;

import com.nimbusds.jose.jwk.JWKSet;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientCache {

    // Key exchange is unauthenticated, so cap how many key ids can be registered;
    // otherwise any caller can grow this map (and the server's key generation) without bound.
    static final int MAX_KEY_IDS = 50_000;

    ConcurrentMap<String,ClientHandler>  idHandlerMap = new ConcurrentHashMap<>();

    public boolean containsAny(JWKSet keySet)
    {
        return keySet.getKeys().stream().anyMatch(key -> key.getKeyID() == null || idHandlerMap.containsKey(key.getKeyID()));
    }

    public boolean hasCapacityFor(int keyIds)
    {
        return idHandlerMap.size() + keyIds <= MAX_KEY_IDS;
    }

    public void put(String keyId , ClientHandler clientHandler)
    {
        idHandlerMap.put(keyId , clientHandler);
    }


    public Optional<ClientHandler> get(String keyId)
    {

        ClientHandler clientHandler = idHandlerMap.get(keyId);

        if (clientHandler!=null)
        {
            return Optional.of(clientHandler);

        }
        else
        {
            return Optional.empty();
        }
    }

    public void put(JWKSet keySet, ClientHandler clientHandler) {

        // putIfAbsent: a caller must not be able to hijack a key id that already belongs to another client
        keySet.getKeys().forEach(key->idHandlerMap.putIfAbsent(key.getKeyID(),clientHandler));
    }
}
