package otp;

import otp.sms.EmailSMS;
import otp.sms.PhoneSMS;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OTPManager {

    EmailSMS emailSMS = new EmailSMS();
    PhoneSMS phoneSMS = new PhoneSMS();

    ConcurrentMap<String,String> idotpMap = new ConcurrentHashMap<>();

    /*TODO - add to map , create a PQ and scheduled thread to check for expiry and remove if expired, check expiry before validating otp ,
    create completable future for email and sms processing

     */

    public OTPResponse generateOTP(OTPRequest request) {

        OTPResponse response = new OTPResponse();
        switch (request.getType())
        {
            case Email:
                response.setMessageId(emailSMS.send(request.getPhoneNumOrEmail()));
                break;
            case Phone:
                response.setMessageId(phoneSMS.send(request.getPhoneNumOrEmail()));

        }

        return response;

    }
}
