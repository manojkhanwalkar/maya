import data.Token;
import org.junit.Test;
import util.CardTuple;
import util.CardUtil;
import util.FPECardTuple;
import util.HashCardTuple;
import vault.TokenManager;

import java.security.SecureRandom;
import java.util.Optional;

import static util.CardUtil.*;
import static util.CardUtil.SHA3HashWithSalt;

public class CardTester {


    public static void main(String[] args) {

     //  test();

       test1();


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
