package otp;

import data.Attributes;
import data.AttributesBuilder;
import data.PMRequest;
import data.PMResponse;
import util.Connection;
import util.JSONUtil;

import java.util.Optional;

public class OTPClient {




    public static OTPResponse submit(OTPRequest request)
    {

        Connection app = new Connection("https://localhost:10080/");

        Optional<String> result = app.sendSimple(JSONUtil.toJSON(request),"otp");

       System.out.println(result);

       if (result.isPresent())
       {
           return (OTPResponse) JSONUtil.fromJSON(result.get(), OTPResponse.class);
       }
      else
       {
           return null;
       }

    }


    public static void main(String[] args) {

        {
            OTPRequest request = new OTPRequest();
            request.setPhoneNumOrEmail("+17327620499");
            request.setType(OTPRequest.Type.Phone);

            OTPResponse pmResponse = OTPClient.submit(request);

            System.out.println(pmResponse);
        }
        {

            OTPRequest request = new OTPRequest();
            request.setPhoneNumOrEmail("manoj_khanwalkar@hotmail.com");
            request.setType(OTPRequest.Type.Email);

            OTPResponse pmResponse = OTPClient.submit(request);

            System.out.println(pmResponse);

        }

    }


}
