import util.CardTuple;
import util.CardUtil;
import util.FPECardTuple;
import util.HashCardTuple;

import java.security.SecureRandom;

import static util.CardUtil.*;
import static util.CardUtil.SHA3HashWithSalt;

public class CardTester {


    public static void main(String[] args) {

       test();


    }


    public static void test()
    {


        String str = CardUtil.createVCard();


        FPECardTuple cardTuple = FPEencrypt1(str);

        String newText = FPEdecrypt1(cardTuple);


        System.out.println(str + "  " + cardTuple.getEncryptedText() + "  " + newText);

        System.out.println("Card is valid " + CardUtil.validateCard(newText)) ;

        HashCardTuple hashCardTuple = SHA3HashWithSalt(str);

        var result = ValidateSHA3HashWithSalt(str,hashCardTuple);

        System.out.println(result);



    }
}
