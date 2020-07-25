package client;


import data.Attributes;
import data.AttributesBuilder;
import data.RequestBuilder;

import java.util.UUID;

public class SenderReceiverTest {


    public static void main(String[] args) throws Exception  {


        Sender sender = new Sender();

        sender.init();

   /*     AttributesBuilder attributesBuilder = new AttributesBuilder();

        Attributes attributes = attributesBuilder.birthDate("1980-1-1").cardNumber("1588213088725991").email("ttt@ttt.com").familyName("Hawking").givenName("Elm")
                .phoneNumber("+18002003456").residentAddress("Univ street").residentCity("Princeton").residentCountry("USA")
                .residentPostalCode("08836").residentState("CA").build();

        RequestBuilder requestBuilder = new RequestBuilder();

        requestBuilder.attributes(attributes).header(UUID.randomUUID().toString()).workflowId(UUID.randomUUID().toString());


        sender.sendMessagetoCCVId(requestBuilder.build());


*/



    }
}
