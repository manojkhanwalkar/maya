import data.Token;
import org.junit.Test;
import util.CardTuple;
import util.CardUtil;
import util.FPECardTuple;
import util.HashCardTuple;
import vault.TokenManager;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static util.CardUtil.*;
import static util.CardUtil.SHA3HashWithSalt;

public class CardTester {


    public static void main(String[] args) {

     //  test();

      // test1();

        // test3();

        String str = LocalDate.now().plusYears(4).toString();

        System.out.println(str);

       LocalDate test =  LocalDate.parse(str);

       System.out.println(test);

    }

    public static void test3()
    {
        String card = CardUtil.createVCard();

        long start = System.nanoTime();

        Set<String> cards = CardUtil.possibleCards(card.substring(0,6), card.substring(12,16));

        long end = System.nanoTime();

        System.out.println("Time taken in nanos " + (end-start));

        System.out.println(card);
        System.out.println(cards.size());

        System.out.println(cards.contains(card));

    }



    @Test
    public static void test1()
    {
        TokenManager tokenManager = new TokenManager();

        String str = CardUtil.createVCard();

        Optional<Token> token =
                tokenManager.put("Merchant1",str, Token.Type.FPE1);

        System.out.println(str + "  " + token.get().getValue() + " " + tokenManager.get("Merchant1",token.get()));

        token =
                tokenManager.put("Merchant1",str, Token.Type.FPE);
        System.out.println(str + "  " + token.get().getValue() + " " + tokenManager.get("Merchant1",token.get()));

        token =
                tokenManager.put("Merchant1",str, Token.Type.Hash);
        System.out.println(str + "  " + token.get().getValue() + " " + tokenManager.get("Merchant1",token.get()));

      /*  token =
                tokenManager.put("Merchant1",str, Token.Type.HashF6L4);
        System.out.println(str + "  " + token.get().getValue() + " " + tokenManager.get("Merchant1",token.get()));
*/


    }


    @Test
    public static void test()
    {


        String str = CardUtil.createVCard();


        FPECardTuple cardTuple = FPEencrypt1(str);

        String newText = FPEdecrypt1(cardTuple);


        System.out.println(str + "  " + cardTuple.getEncryptedText() + "  " + newText);

        System.out.println("Card is valid " + CardUtil.validateCard(newText)) ;

        HashCardTuple hashCardTuple = SHA3HashWithSalt(str);

        var result = ValidateSHA3HashWithSalt(hashCardTuple);

        System.out.println(result);



    }
}
