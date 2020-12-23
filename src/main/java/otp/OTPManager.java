package otp;

import lombok.SneakyThrows;
import otp.sms.EmailSMS;
import otp.sms.PhoneSMS;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static otp.sms.SMSUtil.OTPString;

public class OTPManager {

    EmailSMS emailSMS = new EmailSMS();
    PhoneSMS phoneSMS = new PhoneSMS();

    ConcurrentMap<String,IdOTPTimeTuple> idotpexpiryMap = new ConcurrentHashMap<>();

    /*TODO  , create a PQ and scheduled thread to check for expiry and remove if expired, check expiry before validating otp ,


     */

    @SneakyThrows
    public OTPResponse generateOTP(OTPRequest request) {


        CompletableFuture<OTPResponse> futureResponse =null;

        String otp = OTPString();
        switch (request.getType())
        {
            case Email:
                    futureResponse = CompletableFuture.supplyAsync(()->{
                    OTPResponse response = new OTPResponse();
                    response.setMessageId(emailSMS.send(request.getPhoneNumOrEmail(),otp));
                    return response;
                });

                break;
            case Phone:
                futureResponse = CompletableFuture.supplyAsync(()->{
                    OTPResponse response = new OTPResponse();
                    response.setMessageId(phoneSMS.send(request.getPhoneNumOrEmail(),otp));
                    return response;
                });


        }

        long expiryTime = System.currentTimeMillis() + 1000;
        OTPResponse response = futureResponse.get();
        var idOtpExpiryTuple = new IdOTPTimeTuple(response.getMessageId(),otp,expiryTime );

        idotpexpiryMap.put(response.getMessageId(),idOtpExpiryTuple);

        return response;

    }

    public OTPValidateResponse validateOTP(OTPValidate request) {

        OTPValidateResponse response = new OTPValidateResponse();
        response.setMessageId(request.getMessageId());
        var idOtpExpiryTuple = idotpexpiryMap.remove(request.getMessageId());
        if (idOtpExpiryTuple!=null)
        {
            if (idOtpExpiryTuple.getExpiryTime()<System.currentTimeMillis())
            {
                response.setResponse(OTPValidateResponse.Response.Expired);
            }
            else
            {
                response.setResponse(OTPValidateResponse.Response.Valid);
            }

        }
        else
        {
            response.setResponse(OTPValidateResponse.Response.Invalid);
        }

        return response;

    }
}
