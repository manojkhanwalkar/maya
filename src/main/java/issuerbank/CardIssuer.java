package issuerbank;

import common.Processor;
import data.*;
import util.CardUtil;
import util.JSONUtil;
import vault.TokenManager;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class CardIssuer implements Processor<String,String> {




    @Override
    public String process(String payloadUnwrapped) {

        Card card = new Card();
        card.setPan(CardUtil.createVCard());
        card.setExpiryDate(LocalDate.now().plusYears(4).toString());
        card.setSecCode(CardUtil.securityCode(3));


        CardRequest cardRequest = (CardRequest) JSONUtil.fromJSON(payloadUnwrapped, CardRequest.class);

        CardResponse cardResponse = new CardResponse();
        cardResponse.setCard(card);
        cardResponse.setRequestId(cardRequest.getRequestId());
        cardResponse.setResponseId(UUID.randomUUID().toString());
        cardResponse.setUserId(cardRequest.getUserId());

        return JSONUtil.toJSON(cardResponse);



    }



}
