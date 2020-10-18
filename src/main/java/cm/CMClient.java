package cm;


import data.*;
import util.Connection;
import util.JSONUtil;
import util.Sender;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CMClient {


    public static void main(String[] args) throws Exception  {

        String url = "https://localhost:8180/";

        Connection app = new Connection(url);




        ClickRequest request = new ClickRequest("User1");


        String payload = JSONUtil.toJSON(request);


        HttpResponse response = app.post1(payload,"click");

        if (response.statusCode()==303)
        {
            String newurl = response.headers().firstValue("location").get();

            String clickResponse = response.headers().firstValue("ClickResponse").get();

            var orequest = JSONUtil.toJSON(new OfferRequest());

            response =  app.post1(orequest,newurl, Map.of("ClickResponse",clickResponse));

            System.out.println(response);


        }



        //System.out.println("Result is " + result);

      //  CCVIdResponse response = (CCVIdResponse) JSONUtil.fromJSON(responseStr, CCVIdResponse.class);

        //System.out.println(response);



    }



}
