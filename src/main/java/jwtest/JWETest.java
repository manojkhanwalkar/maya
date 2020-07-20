package jwtest;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.PublicJsonWebKey;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static jwtest.JWSTest.*;
import static util.JWUtil.*;


public class JWETest {

    public static void main(String[] args) throws Exception {



      //  System.out.println(decrypt(key,verify(keyPair.getPublic(),str).orElseThrow()));

        //testECDH();
    }


    private static void test1() throws Exception
    {
        SecretKey key = getAESKey(256);

        System.out.println("Secret key generated");

        String payload = "JWE and JWS demonstrator";


        KeyPair keyPair = generateTestKeys();




        String str = encrypt(key,sign(keyPair.getPrivate(),payload,"keyId"));

        System.out.println(str);


        System.out.println(verify(keyPair.getPublic(),decrypt(key,str)).orElseThrow());
    }


    private static void test2() throws Exception
    {
        SecretKey key = getAESKey(256);

        System.out.println("Secret key generated");

        String payload = "JWE and JWS demonstrator using two JWK's ";

        KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
        gen.initialize(256);
        KeyPair keyPair = gen.generateKeyPair();



        PublicJsonWebKey keyPairForSigning = createECJWKForSigning(keyPair,"keyPairForSigning");


        String str = encrypt(key,sign(keyPairForSigning.getPrivateKey(),payload,"keyId"));


        PublicJsonWebKey keyPairForVerifying = createECJWKForVerifying(keyPair,"keyPairForVerifying");


        System.out.println(verify(keyPairForVerifying.getPublicKey(),decrypt(key,str)).orElseThrow());


        System.out.println(keyPairForSigning.toJson());

        System.out.println(keyPairForVerifying.toJson());

        String receiverJwkJson = "\n{\"kty\":\"EC\",\n" +
                " \"crv\":\"P-256\",\n" +
                " \"x\":\"BHId3zoDv6pDgOUh8rKdloUZ0YumRTcaVDCppUPoYgk\",\n" +
                " \"y\":\"g3QIDhaWEksYtZ9OWjNHn9a6-i_P9o5_NrdISP0VWDU\",\n" +
                " \"d\":\"KpTnMOHEpskXvuXHFCfiRtGUHUZ9Dq5CCcZQ-19rYs4\"\n" +
                "}";
        PublicJsonWebKey receiverJwk = PublicJsonWebKey.Factory.newPublicJwk(receiverJwkJson);

        System.out.println(receiverJwk.toJson());

        System.out.println(receiverJwk);

    }


 /*   private static void testECDH() throws Exception
    {
          String payload = "JWE and JWS demonstrator using ECDH-ES and AES";

        KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
        gen.initialize(256);
        KeyPair keyPair = gen.generateKeyPair();


        gen = KeyPairGenerator.getInstance("EC");
        gen.initialize(256);
        KeyPair keyPair1 = gen.generateKeyPair();


        PublicJsonWebKey keyPairForSigning = createECJWKForSigning(keyPair,"keyPairForSigning");

        PublicJsonWebKey keyForEncryption = createECJWKForVerifying(keyPair1,"keyForEncryption");



        String str = encrypt(keyForEncryption,sign(keyPairForSigning.getPrivateKey(),payload,"keyId"));


        PublicJsonWebKey keyPairForVerifying = createECJWKForVerifying(keyPair,"keyPairForVerifying");
        PublicJsonWebKey keyForDecryption = createECJWKForSigning(keyPair1,"keyForDecryption");


        System.out.println(verify(keyPairForVerifying.getPublicKey(),decrypt(keyForDecryption,str)).orElseThrow());



    }*/







    // AES secret key
    public static SecretKey getAESKey(int keysize) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keysize,SecureRandom.getInstanceStrong());
        return keyGen.generateKey();
    }



    private static void test(SecretKey key) throws Exception
    {
        //
        // An example showing the use of JSON Web Encryption (JWE) to encrypt and then decrypt some content
        // using a symmetric key and direct encryption.
        //



        // The content to be encrypted
        String message = "Well, as of this moment, they're on DOUBLE SECRET PROBATION!";

        // The shared secret or shared symmetric key represented as a octet sequence JSON Web Key (JWK)
        String jwkJson = "{\"kty\":\"oct\",\"k\":\"Fdh9u8rINxfivbrianbbVT1u232VQBZYKx1HGAGPt2I\"}";
        JsonWebKey jwk = JsonWebKey.Factory.newJwk(jwkJson);

        // Create a new Json Web Encryption object
        JsonWebEncryption senderJwe = new JsonWebEncryption();

        // The plaintext of the JWE is the message that we want to encrypt.
        senderJwe.setPlaintext(message);

        // Set the "alg" header, which indicates the key management mode for this JWE.
        // In this example we are using the direct key management mode, which means
        // the given key will be used directly as the content encryption key.
        senderJwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT);

        // Set the "enc" header, which indicates the content encryption algorithm to be used.
        // This example is using AES_128_CBC_HMAC_SHA_256 which is a composition of AES CBC
        // and HMAC SHA2 that provides authenticated encryption.
        senderJwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);

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
        String compactSerialization = senderJwe.getCompactSerialization();

        // Do something with the JWE. Like send it to some other party over the clouds
        // and through the interwebs.
        System.out.println("JWE compact serialization: " + compactSerialization);

        // That other party, the clientHandler, can then use JsonWebEncryption to decrypt the message.
        JsonWebEncryption receiverJwe = new JsonWebEncryption();

        // Set the algorithm constraints based on what is agreed upon or expected from the sender
        AlgorithmConstraints algConstraints = new AlgorithmConstraints(org.jose4j.jwa.AlgorithmConstraints.ConstraintType.PERMIT, KeyManagementAlgorithmIdentifiers.DIRECT);
        receiverJwe.setAlgorithmConstraints(algConstraints);
        AlgorithmConstraints encConstraints = new AlgorithmConstraints(org.jose4j.jwa.AlgorithmConstraints.ConstraintType.PERMIT, ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        receiverJwe.setContentEncryptionAlgorithmConstraints(encConstraints);

        // Set the compact serialization on new Json Web Encryption object
        receiverJwe.setCompactSerialization(compactSerialization);

        // Symmetric encryption, like we are doing here, requires that both parties have the same key.
        // The key will have had to have been securely exchanged out-of-band somehow.
        receiverJwe.setKey(key);

        // Get the message that was encrypted in the JWE. This step performs the actual decryption steps.
        String plaintext = receiverJwe.getPlaintextString();

        // And do whatever you need to do with the clear text message.
        System.out.println("plaintext: " + plaintext);
    }


}
