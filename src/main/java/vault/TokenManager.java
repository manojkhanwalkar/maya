package vault;

import data.Token;
import org.checkerframework.checker.nullness.Opt;
import util.CardUtil;
import util.FPECardTuple;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TokenManager {

    //TODO - support both types of tokens . (FPE and Hash )

    ConcurrentMap<String,ConcurrentMap<String,FPECardTuple>>  merchantTokenCCMap = new ConcurrentHashMap<>();


    public Optional<Token> put(String merchantId , String cc )
    {
        ConcurrentMap<String,FPECardTuple> tokenCCMap = merchantTokenCCMap.computeIfAbsent(merchantId,k-> new ConcurrentHashMap<>());

        if (CardUtil.validateCard(cc))
        {
            FPECardTuple cardTuple = CardUtil.FPEencrypt1(cc);
            tokenCCMap.put(cardTuple.getEncryptedText(),cardTuple);

            Token token = new Token();
            token.setType(Token.Type.FPE1);
            token.setValue(cardTuple.getEncryptedText());

            return Optional.of(token);

        }

        return Optional.empty();


    }


    public Optional<String> get(String merchandId , Token token)
    {

        ConcurrentMap<String,FPECardTuple> tokenCCMap = merchantTokenCCMap.get(merchandId);
        if (tokenCCMap!=null)
        {
            FPECardTuple fpeCardTuple = tokenCCMap.get(token.getValue());
            if (fpeCardTuple!=null)
            {
                String pan = CardUtil.FPEdecrypt1(fpeCardTuple);

                return Optional.of(pan);
            }


        }

        return Optional.empty();
    }

}
