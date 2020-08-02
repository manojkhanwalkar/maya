package vault;

import data.Token;
import org.checkerframework.checker.nullness.Opt;
import util.CardTuple;
import util.CardUtil;
import util.FPECardTuple;
import util.HashCardTuple;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TokenManager {

    //TODO - add code for HashF6L4

    ConcurrentMap<String,ConcurrentMap<String, CardTuple>>  merchantTokenCCMap = new ConcurrentHashMap<>();


    public Optional<Token> put(String merchantId , String cc ,Token.Type type)
    {
        ConcurrentMap<String,CardTuple> tokenCCMap = merchantTokenCCMap.computeIfAbsent(merchantId,k-> new ConcurrentHashMap<>());

        if (CardUtil.validateCard(cc))
        {
            switch(type) {

                case FPE1 : {
                    FPECardTuple cardTuple = CardUtil.FPEencrypt1(cc);
                    tokenCCMap.put(cardTuple.getEncryptedText(), cardTuple);

                    Token token = new Token();
                    token.setType(Token.Type.FPE1);
                    token.setValue(cardTuple.getEncryptedText());

                    return Optional.of(token);
                }

                case FPE : {
                    FPECardTuple cardTuple = CardUtil.FPEencrypt(cc);
                    tokenCCMap.put(cardTuple.getEncryptedText(), cardTuple);

                    Token token = new Token();
                    token.setType(Token.Type.FPE);
                    token.setValue(cardTuple.getEncryptedText());

                    return Optional.of(token);
                }

                case Hash: {
                    HashCardTuple cardTuple = CardUtil.SHA3HashWithSalt(cc);
                    tokenCCMap.put(cardTuple.getEncryptedText(), cardTuple);

                    Token token = new Token();
                    token.setType(Token.Type.Hash);
                    token.setValue(cardTuple.getEncryptedText());

                    return Optional.of(token);

                }
            }

        }

        return Optional.empty();


    }


    public Optional<String> get(String merchandId , Token token)
    {

        ConcurrentMap<String,CardTuple> tokenCCMap = merchantTokenCCMap.get(merchandId);
        if (tokenCCMap!=null)
        {
            switch(token.getType())
            {
                case FPE1 : {

                    FPECardTuple fpeCardTuple = (FPECardTuple)tokenCCMap.get(token.getValue());
                    if (fpeCardTuple != null) {
                        String pan = CardUtil.FPEdecrypt1(fpeCardTuple);

                        return Optional.of(pan);
                    }
                }

                case FPE : {
                    FPECardTuple fpeCardTuple = (FPECardTuple)tokenCCMap.get(token.getValue());
                    if (fpeCardTuple != null) {
                        String pan = CardUtil.FPEdecrypt(fpeCardTuple);

                        return Optional.of(pan);
                    }

                }

                case Hash:
                {
                    HashCardTuple hashCardTuple = (HashCardTuple) tokenCCMap.get(token.getValue());
                    if (hashCardTuple != null && CardUtil.ValidateSHA3HashWithSalt(hashCardTuple)) {


                        return Optional.of(hashCardTuple.getPan());
                    }

                }




            }


        }

        return Optional.empty();
    }

}
