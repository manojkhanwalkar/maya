package pm;

import data.*;
import util.Connection;
import util.JSONUtil;
import vfs.TransactionData;

import java.util.Optional;

public class PMClient {




    public static PMResponse submit(Attributes attributes)
    {
        // send to scheduler a jar file and client name and get back a job id .

        // System.out.println(profileId);


        PMRequest pmRequest = new PMRequest();
        pmRequest.setAttributes(attributes);
        Connection app = new Connection("https://localhost:8280/");

        Optional<String> result = app.sendSimple(JSONUtil.toJSON(pmRequest),"verify1");


       if (result.isPresent())
       {
           return (PMResponse) JSONUtil.fromJSON(result.get(), PMResponse.class);
       }
      else
       {
           return PMResponse.getEMPTY();
       }

    }


    public static void main(String[] args) {

        AttributesBuilder attributesBuilder = new AttributesBuilder();

        Attributes attributes = attributesBuilder.birthDate("1980-1-1").cardNumber("1588213088725991").email("ttt@ttt.com").familyName("Hawking").givenName("Elm")
                .phoneNumber("+18002003456").residentAddress("Univ street").residentCity("Princeton").residentCountry("USA")
                .residentPostalCode("08836").residentState("CA").build();
        PMResponse pmResponse = PMClient.submit(attributes);

        System.out.println(pmResponse);

    }


}
