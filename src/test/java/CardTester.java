import util.CardTuple;
import util.CardUtil;

import java.security.SecureRandom;

import static util.CardUtil.*;

public class CardTester {


    public static void main(String[] args) {

       test();


    }


    public static void test()
    {


        String str = CardUtil.createVCard();


        CardTuple cardTuple = FPEencrypt1(str);

        String newText = FPEdecrypt1(cardTuple);


        System.out.println(str + "  " + cardTuple.getEncryptedText() + "  " + newText);

        System.out.println("Card is valid " + CardUtil.validateCard(newText)) ;




    }
}
