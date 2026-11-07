package vault;

import data.Token;
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
        if (merchandId == null || token == null || token.getType() == null || token.getValue() == null)
            return Optional.empty();

        ConcurrentMap<String,CardTuple> tokenCCMap = merchantTokenCCMap.get(merchandId);
        if (tokenCCMap == null)
            return Optional.empty();

        CardTuple tuple = tokenCCMap.get(token.getValue());
        if (tuple == null)
            return Optional.empty();

        // Each case returns; a token must be detokenized only with the scheme it was created with
        // (the old switch fell through, so e.g. an FPE1 lookup could run FPE or hash handling).
        switch (token.getType())
        {
            case FPE1:
                if (tuple instanceof FPECardTuple && tuple.getType() == Token.Type.FPE1)
                    return Optional.of(CardUtil.FPEdecrypt1((FPECardTuple) tuple));
                return Optional.empty();

            case FPE:
                if (tuple instanceof FPECardTuple && tuple.getType() == Token.Type.FPE)
                    return Optional.of(CardUtil.FPEdecrypt((FPECardTuple) tuple));
                return Optional.empty();

            case Hash:
                if (tuple instanceof HashCardTuple && CardUtil.ValidateSHA3HashWithSalt((HashCardTuple) tuple))
                    return Optional.of(((HashCardTuple) tuple).getPan());
                return Optional.empty();

            default:
                return Optional.empty();
        }
    }

}
