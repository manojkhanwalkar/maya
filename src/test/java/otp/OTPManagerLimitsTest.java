package otp;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;

public class OTPManagerLimitsTest {

    @Test
    public void otpIsSixDigits() {
        String code = otp.sms.SMSUtil.OTPString();
        assertTrue(code.matches("[0-9]{6}"));
    }

    @Test
    public void tooManyWrongGuessesInvalidateTheOtp() throws Exception {
        OTPManager manager = newManagerWithoutAws();
        manager.idotpexpiryMap.put("m1", new IdOTPTimeTuple("m1", "123456", System.currentTimeMillis() + 60_000));

        OTPValidate wrong = new OTPValidate();
        wrong.setMessageId("m1");
        wrong.setOTP("000000");
        for (int i = 0; i < OTPManager.MAX_VALIDATION_ATTEMPTS; i++)
            assertEquals(OTPValidateResponse.Response.Invalid, manager.validateOTP(wrong).getResponse());

        OTPValidate right = new OTPValidate();
        right.setMessageId("m1");
        right.setOTP("123456");
        assertEquals(OTPValidateResponse.Response.Invalid, manager.validateOTP(right).getResponse());
    }

    @Test
    public void correctOtpValidatesOnce() throws Exception {
        OTPManager manager = newManagerWithoutAws();
        manager.idotpexpiryMap.put("m2", new IdOTPTimeTuple("m2", "654321", System.currentTimeMillis() + 60_000));
        OTPValidate right = new OTPValidate();
        right.setMessageId("m2");
        right.setOTP("654321");
        assertEquals(OTPValidateResponse.Response.Valid, manager.validateOTP(right).getResponse());
        assertEquals(OTPValidateResponse.Response.Invalid, manager.validateOTP(right).getResponse());
    }

    @Test
    public void expiredOtpsAreRemovedFromTheLookupMap() {
        ConcurrentMap<String, IdOTPTimeTuple> map = new ConcurrentHashMap<>();
        ExpiredOTPProcessor p = new ExpiredOTPProcessor(map);
        IdOTPTimeTuple t = new IdOTPTimeTuple("old", "111111", System.currentTimeMillis() - 1);
        map.put("old", t);
        p.add(t);
        p.cleanup();
        assertFalse(map.containsKey("old"));
        assertFalse(t.toString().contains("111111"));
    }

    private static OTPManager newManagerWithoutAws() throws Exception {
        // The AWS SES/SNS clients need a region only to be constructed; no call is made.
        System.setProperty("aws.region", "us-east-1");
        return new OTPManager();
    }
}
