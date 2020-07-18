package util;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.EllipticCurveJsonWebKey;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.util.Map;
import java.util.Optional;

public class JWUtil {

   // public enum SignType { Complete , Compressed }


    public static void createKeys(Map<String,PublicJsonWebKey> publicJsonWebKeyMap , Map<String,PublicJsonWebKey> privateJsonWebKeyMap,String name)
    {
        for (int i=0;i<10;i++) {
            try {
                KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
                gen.initialize(256);
                KeyPair keyPair = gen.generateKeyPair();


                publicJsonWebKeyMap.put(name+i, createECJWK(keyPair, name+i, false));
                privateJsonWebKeyMap.put(name+i, createECJWK(keyPair, name+i, true));


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static String encrypt(PublicJsonWebKey encrypterKey, String payload) throws Exception
    {

            String alg = KeyManagementAlgorithmIdentifiers.ECDH_ES_A256KW;
            String enc = ContentEncryptionAlgorithmIdentifiers.AES_128_GCM;


            JsonWebEncryption jwe = new JsonWebEncryption();


            jwe.setAlgorithmHeaderValue(alg);
            jwe.setEncryptionMethodHeaderParameter(enc);
            jwe.setPlaintext(payload);

            jwe.setKey(encrypterKey.getPublicKey());

            jwe.setContentTypeHeaderValue("JWT");

            jwe.setKeyIdHeaderValue(encrypterKey.getKeyId());


        String compactSerialization = jwe.getCompactSerialization();

           // System.out.println(jwe);

            return compactSerialization;

    }

    public static String decrypt(Map<String,PublicJsonWebKey> keyMap,  String payload) throws Exception
    {

  //      String alg = KeyManagementAlgorithmIdentifiers.ECDH_ES_A256KW;
    //    String enc = ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256;


        JsonWebEncryption receiverJwe = new JsonWebEncryption();
        receiverJwe.setCompactSerialization(payload);


        String keyId = receiverJwe.getKeyIdHeaderValue();

        PublicJsonWebKey decrypterKey = keyMap.get(keyId);


                //  System.out.println(receiverJwe.getHeaders().getFullHeaderAsJsonString());


        receiverJwe.setKey(decrypterKey.getPrivateKey());

        String result = receiverJwe.getPlaintextString();


        return result;

    }

    @Deprecated
    public static String decrypt(PublicJsonWebKey decrypterKey,  String payload) throws Exception
    {

        //      String alg = KeyManagementAlgorithmIdentifiers.ECDH_ES_A256KW;
        //    String enc = ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256;


        JsonWebEncryption receiverJwe = new JsonWebEncryption();
        receiverJwe.setCompactSerialization(payload);





        //  System.out.println(receiverJwe.getHeaders().getFullHeaderAsJsonString());


        receiverJwe.setKey(decrypterKey.getPrivateKey());

        String result = receiverJwe.getPlaintextString();


        return result;

    }






    public static String sign(PublicJsonWebKey jwk , String payload) throws JoseException {


        return sign(jwk.getPrivateKey(),payload, jwk.getKeyId());
    }



    public static String sign(PrivateKey privateKey , String payload,String keyId) throws JoseException {

            // Create a new JsonWebSignature
            JsonWebSignature jws = new JsonWebSignature();

            // Set the payload, or signed content, on the JWS object
            jws.setPayload(payload);

            // Set the signature algorithm on the JWS that will integrity protect the payload
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);


            jws.setKeyIdHeaderValue(keyId);


        jws.setKey(privateKey);

            // Sign the JWS and produce the compact serialization or complete JWS representation, which
            // is a string consisting of three dot ('.') separated base64url-encoded
            // parts in the form Header.Payload.Signature




                return jws.getCompactSerialization();


    }


    @Deprecated
    public static Optional<String> verify(PublicJsonWebKey publicKey,String compactSerialization) throws JoseException
    {

        return verify(publicKey.getPublicKey(),compactSerialization);
    }



    public static Optional<String> verify(Map<String, JsonWebKey> keys , String compactSerialization) throws JoseException
    {
        JsonWebSignature jws = new JsonWebSignature();

        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256));

        jws.setCompactSerialization(compactSerialization);

        //     System.out.println("Signature Key id is " + jws.getKeyIdHeaderValue());
        //   System.out.println(jws.getHeaders().getFullHeaderAsJsonString());


        String keyId = jws.getKeyIdHeaderValue();

        PublicJsonWebKey publicKey =(PublicJsonWebKey) keys.get(keyId);

        // Set the verification key
        // Note that your application will need to determine where/how to get the key
        // Here we use an example from the JWS spec
        //PublicKey publicKey = ExampleEcKeysFromJws.PUBLIC_256;
        jws.setKey(publicKey.getPublicKey());



        // Check the signature
        if (jws.verifySignature())
            return Optional.of(jws.getPayload());
        else
            return Optional.empty();


    }


    @Deprecated
    public static Optional<String> verify(PublicKey publicKey,String compactSerialization) throws JoseException
    {


        JsonWebSignature jws = new JsonWebSignature();

        // Set the algorithm constraints based on what is agreed upon or expected from the sender
        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256));

        // Set the compact serialization on the JWS
        jws.setCompactSerialization(compactSerialization);

        //     System.out.println("Signature Key id is " + jws.getKeyIdHeaderValue());
        //   System.out.println(jws.getHeaders().getFullHeaderAsJsonString());



        // Set the verification key
        // Note that your application will need to determine where/how to get the key
        // Here we use an example from the JWS spec
        //PublicKey publicKey = ExampleEcKeysFromJws.PUBLIC_256;
        jws.setKey(publicKey);



        // Check the signature
        if (jws.verifySignature())
            return Optional.of(jws.getPayload());
        else
            return Optional.empty();



    }

    public static PublicJsonWebKey createECJWK(KeyPair keyPair, String keyId, boolean includePrivate) throws Exception
    {

        ECPublicKey ecPublicKey = (ECPublicKey)keyPair.getPublic();

        EllipticCurveJsonWebKey jwk = new EllipticCurveJsonWebKey(ecPublicKey);

        jwk.setKeyId(keyId);

        if (includePrivate)
            jwk.setPrivateKey(keyPair.getPrivate());


        return jwk;
    }





    public static String encrypt(SecretKey key, String payload) throws JoseException
    {
        JsonWebEncryption senderJwe = new JsonWebEncryption();

        // The plaintext of the JWE is the message that we want to encrypt.
        senderJwe.setPlaintext(payload);

        // Set the "alg" header, which indicates the key management mode for this JWE.
        // In this example we are using the direct key management mode, which means
        // the given key will be used directly as the content encryption key.
        senderJwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A256KW);

        // Set the "enc" header, which indicates the content encryption algorithm to be used.
        // This example is using AES_128_CBC_HMAC_SHA_256 which is a composition of AES CBC
        // and HMAC SHA2 that provides authenticated encryption.
        senderJwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512);

        // Set the key on the JWE. In this case, using direct mode, the key will used directly as
        // the content encryption key. AES_128_CBC_HMAC_SHA_256, which is being used to encrypt the
        // content requires a 256 bit key.
        senderJwe.setKey(key);

        // Produce the JWE compact serialization, which is where the actual encryption is done.
        // The JWE compact serialization consists of five base64url encoded parts
        // combined with a dot ('.') character in the general format of
        // <header>.<encrypted key>.<initialization vector>.<ciphertext>.<authentication tag>
        // Direct encryption doesn't use an encrypted key so that field will be an empty string
        // in this case.
        return senderJwe.getCompactSerialization();


    }


    public static String decrypt(SecretKey key, String encryptedPayload) throws JoseException
    {   // That other party, the receiver, can then use JsonWebEncryption to decrypt the message.
        JsonWebEncryption receiverJwe = new JsonWebEncryption();

        // Set the algorithm constraints based on what is agreed upon or expected from the sender
        AlgorithmConstraints algConstraints = new AlgorithmConstraints(org.jose4j.jwa.AlgorithmConstraints.ConstraintType.PERMIT, KeyManagementAlgorithmIdentifiers.A256KW);
        receiverJwe.setAlgorithmConstraints(algConstraints);
        AlgorithmConstraints encConstraints = new AlgorithmConstraints(org.jose4j.jwa.AlgorithmConstraints.ConstraintType.PERMIT, ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512);
        receiverJwe.setContentEncryptionAlgorithmConstraints(encConstraints);

        // Set the compact serialization on new Json Web Encryption object
        receiverJwe.setCompactSerialization(encryptedPayload);

        // Symmetric encryption, like we are doing here, requires that both parties have the same key.
        // The key will have had to have been securely exchanged out-of-band somehow.
        receiverJwe.setKey(key);

        // Get the message that was encrypted in the JWE. This step performs the actual decryption steps.
        return receiverJwe.getPlaintextString();


    }




}
