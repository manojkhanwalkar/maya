package jwtest;

import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.PublicJsonWebKey;
import util.JWUtil;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static jwtest.JWSTest.createECJWKForSigning;
import static jwtest.JWSTest.createECJWKForVerifying;
import static util.JWUtil.*;

public class Sender {


/*    PublicJsonWebKey kaPubKSender;
    PublicJsonWebKey kaPrivKSender;
    PublicJsonWebKey signPubKSender;
    PublicJsonWebKey signPrivKSender;*/

    Map<String,PublicJsonWebKey> kaPubKSenderMap = new HashMap<>();
    Map<String,PublicJsonWebKey> kaPrivKSenderMap = new HashMap<>();
    Map<String,PublicJsonWebKey> signPubKSenderMap = new HashMap<>();
    Map<String,PublicJsonWebKey> signPrivKSenderMap = new HashMap<>();





    public JsonWebKeySet getKAandSignPubKeys()
    {


        JsonWebKeySet set   = new JsonWebKeySet();

        kaPubKSenderMap.values().stream().forEach(key-> set.addJsonWebKey(key));
        signPubKSenderMap.values().stream().forEach(key-> set.addJsonWebKey(key));

        return set;
    }

    ClientHandler clientHandler;


//TODO - use static keys and then use JWKS to get the static keys and then do key rotation

    private void createKeys()
    {
        JWUtil.createKeys(signPubKSenderMap,signPrivKSenderMap,"signSender");

        JWUtil.createKeys(kaPubKSenderMap,kaPrivKSenderMap,"kaSender");


    }





    String clientId;

    public Sender(String clientId)
    {

        this.clientId = clientId;
        createKeys();

    }

    Map<String,JsonWebKey> signreceiverKeys = new HashMap<>();
    Map<String,JsonWebKey> kareceiverKeys = new HashMap<>();

    public void init(ClientHandler clientHandler) throws Exception
    {

        JsonWebKeySet set = clientHandler.getKAandSignPubKeys();

        set.getJsonWebKeys().stream().filter(key->key.getKeyId().startsWith("sign")).forEach(key->{ signreceiverKeys.put(key.getKeyId(),key);});
        set.getJsonWebKeys().stream().filter(key->key.getKeyId().startsWith("ka")).forEach(key->{ kareceiverKeys.put(key.getKeyId(),key);});

       // kaPubKReceiver = (PublicJsonWebKey)set.findJsonWebKey("kaPubKReceiver",null,null,null);
        //signPubKReceiver =(PublicJsonWebKey)set.findJsonWebKey("signPubKReceiver",null,null,null);

        this.clientHandler = clientHandler;


    }



    public void sendMessagetoReceiver(String payload) throws Exception
    {
        Random random = new Random();

        int max = random.nextInt(kareceiverKeys.size());

        PublicJsonWebKey kaPubKReceiver = (PublicJsonWebKey)kareceiverKeys.values().stream().collect(Collectors.toList()).get(max);

        max = random.nextInt(kareceiverKeys.size());

        PublicJsonWebKey signPrivKSender = signPrivKSenderMap.values().stream().collect(Collectors.toList()).get(max);

        System.out.println("Encrypt key " + kaPubKReceiver.getKeyId() + " " + "Sign Key " + signPrivKSender.getKeyId());

        String str = encrypt(kaPubKReceiver,sign(signPrivKSender,payload));

        clientHandler.recvMessageFromSender(str);
    }


    public void recvMessageFromReceover(String message) throws Exception
    {
        String decryptedMsg = decrypt(kaPrivKSenderMap,message);

       // System.out.println(decryptedMsg);

//        String payload = verify(signPubKReceiver.getPublicKey(),decryptedMsg).get();

        String payload = verify(signreceiverKeys,decryptedMsg).get();


        System.out.println(payload);

    }





}
