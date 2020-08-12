package client;


import data.*;
import util.JSONUtil;
import util.Sender;

import java.util.UUID;

public class IssuerClient {


    public static void main(String[] args) throws Exception  {


        Sender sender = new Sender("https://localhost:9280/");

        sender.init();


        CardRequest cardRequest = new CardRequest();
        cardRequest.setRequestId(UUID.randomUUID().toString());
        cardRequest.setUserId("User1");

        cardRequest.setName("Joe SizPack");


        String payload = JSONUtil.toJSON(cardRequest);


        String responseStr = sender.send(payload,"issue");

        CardResponse response = (CardResponse) JSONUtil.fromJSON(responseStr, CardResponse.class);

        System.out.println(response);



    }



}
