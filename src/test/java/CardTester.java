import util.CardUtil;

import java.security.SecureRandom;

import static util.CardUtil.FPEdecrypt;
import static util.CardUtil.FPEencrypt;

public class CardTester {


    public static void main(String[] args) {

       test();


    }


    public static void test()
    {

        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];


        byte[] tweak = new byte[10];


        secureRandom.nextBytes(key);


        secureRandom.nextBytes(tweak);

        String str = CardUtil.createVCard();


        String cipherText = FPEencrypt(str,key,tweak);

        String newText = FPEdecrypt(cipherText,key,tweak);


        System.out.println(str + "  " + cipherText + "  " + newText);

        System.out.println("Card is valid " + CardUtil.validateCard(newText)) ;




    }
}
