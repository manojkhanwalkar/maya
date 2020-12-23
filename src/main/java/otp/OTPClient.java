package otp;

import data.Attributes;
import data.AttributesBuilder;
import data.PMRequest;
import data.PMResponse;
import lombok.SneakyThrows;
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



    public static OTPValidateResponse submit(OTPValidate request)
    {

        Connection app = new Connection("https://localhost:10080/");

        Optional<String> result = app.sendSimple(JSONUtil.toJSON(request),"verify");

        System.out.println(result);

        if (result.isPresent())
        {
            return (OTPValidateResponse) JSONUtil.fromJSON(result.get(), OTPValidateResponse.class);
        }
        else
        {
            return null;
        }

    }


    @SneakyThrows
    public static void main(String[] args) {

      /*  {
            OTPRequest request = new OTPRequest();
            request.setPhoneNumOrEmail("+17327620499");
            request.setType(OTPRequest.Type.Phone);

            OTPResponse pmResponse = OTPClient.submit(request);

            System.out.println(pmResponse);
        }*/

        OTPResponse emailResponse ;
        {

            OTPRequest request = new OTPRequest();
            request.setPhoneNumOrEmail("manoj_khanwalkar@hotmail.com");
            request.setType(OTPRequest.Type.Email);

             emailResponse = OTPClient.submit(request);

            System.out.println(emailResponse);

        }

        // valid message , valid OTP test

    /*    {

            OTPValidate request = new OTPValidate();
            request.setMessageId("0100017690007645-fd65ad7e-5235-4b28-bd3f-c38eb1a76d24-000000");
            request.setOTP("003408");

            OTPValidateResponse pmResponse = OTPClient.submit(request);

            System.out.println(pmResponse);

        }*/

   /*     // valid message id , invalid otp test
        {

            OTPValidate request = new OTPValidate();
            request.setMessageId(emailResponse.getMessageId());
            request.setOTP("test");

            OTPValidateResponse pmResponse = OTPClient.submit(request);

            System.out.println(pmResponse);

        }

        // invalid message id test
       {

            OTPValidate request = new OTPValidate();
            request.setMessageId("anything");
            request.setOTP("test");

            OTPValidateResponse pmResponse = OTPClient.submit(request);

            System.out.println(pmResponse);

        }



        {

            OTPRequest request = new OTPRequest();
            request.setPhoneNumOrEmail("manoj_khanwalkar@hotmail.com");
            request.setType(OTPRequest.Type.Email);

            emailResponse = OTPClient.submit(request);

            System.out.println(emailResponse);

        }

      Thread.sleep(65000);

        // valid message id , expired test
        {

            OTPValidate request = new OTPValidate();
            request.setMessageId(emailResponse.getMessageId());
            request.setOTP("test");

            OTPValidateResponse pmResponse = OTPClient.submit(request);

            System.out.println(pmResponse);

        }*/



    }




}
