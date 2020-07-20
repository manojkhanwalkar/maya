package jwtest;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwx.HeaderParameterNames;
import util.JSONUtil;
import util.JWUtil;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static jwtest.JWSTest.createECJWKForSigning;
import static jwtest.JWSTest.createECJWKForVerifying;
import static util.JWUtil.*;
import static util.JWUtil.decrypt;

public class ClientHandler {


  /*  PublicJsonWebKey kaPubKReceiver;
    PublicJsonWebKey kaPrivKReceiver;
    PublicJsonWebKey signPubKReceiver;
    PublicJsonWebKey signPrivKReceiver;*/


    Map<String,PublicJsonWebKey> kaPubKReceiverMap = new HashMap<>();
    Map<String,PublicJsonWebKey> kaPrivKReceiverMap = new HashMap<>();
    Map<String,PublicJsonWebKey> signPubKReceiverMap = new HashMap<>();
    Map<String,PublicJsonWebKey> signPrivKReceiverMap = new HashMap<>();


    public String getKAandSignPubKeys()
    {
     //   JsonWebKeySet set = new JsonWebKeySet(kaPubKReceiver,signPubKReceiver);

        JsonWebKeySet set   = new JsonWebKeySet();

        kaPubKReceiverMap.values().stream().forEach(key-> set.addJsonWebKey(key));
        signPubKReceiverMap.values().stream().forEach(key-> set.addJsonWebKey(key));

        //   System.out.println(set.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE));



         return set.toJson();
    }

    Sender sender;


//TODO - use static keys and then use JWKS to get the static keys and then do key rotation

    Map<String,JsonWebKey> signSenderKeys = new HashMap<>();
    Map<String,JsonWebKey> kaSenderKeys = new HashMap<>();


    public void init(Sender sender) throws Exception
    {

        String jsonWebKeySet = sender.getKAandSignPubKeys();
        JsonWebKeySet set = new JsonWebKeySet(jsonWebKeySet);


        set.getJsonWebKeys().stream().filter(key->key.getKeyId().startsWith("sign")).forEach(key->{ signSenderKeys.put(key.getKeyId(),key);});
        set.getJsonWebKeys().stream().filter(key->key.getKeyId().startsWith("ka")).forEach(key->{ kaSenderKeys.put(key.getKeyId(),key);});


        this.sender = sender;




    }

    private void createKeys()
    {
            JWUtil.createKeys(signPubKReceiverMap,signPrivKReceiverMap,"signReceiver");

            JWUtil.createKeys(kaPubKReceiverMap,kaPrivKReceiverMap,"kaReceiver");


    }



    public ClientHandler()
    {
        createKeys();
    }

    public void recvMessageFromSender(String message) throws Exception
    {

        System.out.println(verify(signSenderKeys,decrypt(kaPrivKReceiverMap,message)).orElseThrow());

        sendMessageToSender("Received message ");

       // alternate(message);

    }


    public void sendMessageToSender(String payload) throws Exception
    {

        PublicJsonWebKey kaPubKSender = (PublicJsonWebKey)kaSenderKeys.values().stream().findAny().get();

        PublicJsonWebKey signPrivKReceiver = signPrivKReceiverMap.values().stream().findAny().get();

        //   jwe.setHeader(HeaderParameterNames.AGREEMENT_PARTY_U_INFO, UUID.randomUUID().toString());
        //        jwe.setHeader(HeaderParameterNames.AGREEMENT_PARTY_V_INFO, UUID.randomUUID().toString());


        Header header = new Header(HeaderParameterNames.AGREEMENT_PARTY_V_INFO, UUID.randomUUID().toString());
        var headers = List.of(header);

        String str = encrypt(headers , kaPubKSender,sign(signPrivKReceiver,payload));

        sender.recvMessageFromReceover(str);

    }


    @Deprecated
    private void alternate(String message)
    {
        // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
        // be used to validate and process the JWT.
        // The specific validation requirements for a JWT are context dependent, however,
        // it typically advisable to require a (reasonable) expiration time, a trusted issuer, and
        // and audience that identifies your system as the intended recipient.
        // It is also typically good to allow only the expected algorithm(s) in the given context
    /*    AlgorithmConstraints jwsAlgConstraints = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);

        AlgorithmConstraints jweAlgConstraints = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                KeyManagementAlgorithmIdentifiers.ECDH_ES_A256KW);

        AlgorithmConstraints jweEncConstraints = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                ContentEncryptionAlgorithmIdentifiers.AES_128_GCM);

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                //.setRequireExpirationTime() // the JWT must have an expiration time
                //.setMaxFutureValidityInMinutes(300) // but the  expiration time can't be too crazy
                //.setRequireSubject() // the JWT must have a subject claim
                //.setExpectedIssuer("sender") // whom the JWT needs to have been issued by
                //.setExpectedAudience("clientHandler") // to whom the JWT is intended for
                .setDecryptionKey(kaPrivKReceiver.getPrivateKey()) // decrypt with the clientHandler's private key
                .setVerificationKey(signPubKSender.getPublicKey()) // verify the signature with the sender's public key
                //.setJwsAlgorithmConstraints(jwsAlgConstraints) // limits the acceptable signature algorithm(s)
                //.setJweAlgorithmConstraints(jweAlgConstraints) // limits acceptable encryption key establishment algorithm(s)
                //.setJweContentEncryptionAlgorithmConstraints(jweEncConstraints) // limits acceptable content encryption algorithm(s)
                .build(); // create the JwtConsumer instance

        try
        {
            //  Validate the JWT and process it to the Claims
            JwtClaims jwtClaims = jwtConsumer.processToClaims(message);
            System.out.println("JWT validation succeeded! " + jwtClaims);
        }
        catch (InvalidJwtException e)
        {e.printStackTrace();
        }*/
    }





}
