package client;


import data.*;
import util.JSONUtil;
import util.Sender;

import java.util.UUID;

public class SenderReceiverTest {


    public static void main(String[] args) throws Exception  {


        Sender sender = new Sender("https://localhost:8180/");

        sender.init();

       AttributesBuilder attributesBuilder = new AttributesBuilder();

        Attributes attributes = attributesBuilder.birthDate("1980-1-1").cardNumber("1588213088725991").email("ttt@ttt.com").familyName("Hawking").givenName("Elm")
                .phoneNumber("+18002003456").residentAddress("Univ street").residentCity("Princeton").residentCountry("USA")
                .residentPostalCode("08836").residentState("CA").build();

        RequestBuilder requestBuilder = new RequestBuilder();

        requestBuilder.attributes(attributes).header(UUID.randomUUID().toString()).workflowId(UUID.randomUUID().toString());




        CCVIdRequest request = requestBuilder.build();



        String payload = JSONUtil.toJSON(request);


        String responseStr = sender.send(payload,"verify");

        CCVIdResponse response = (CCVIdResponse) JSONUtil.fromJSON(responseStr, CCVIdResponse.class);

        System.out.println(response);



    }



}
