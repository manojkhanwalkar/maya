package otp;

import lombok.SneakyThrows;
import otp.sms.EmailSMS;
import otp.sms.PhoneSMS;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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

    // Wrong guesses allowed per OTP before it is invalidated (a 6-digit OTP must not be brute-forced).
    static final int MAX_VALIDATION_ATTEMPTS = 5;

    // At most MAX_SENDS_PER_WINDOW OTPs per recipient per SEND_WINDOW_MILLIS, so the
    // unauthenticated /otp endpoint cannot be used to flood a phone or mailbox (SMS pumping).
    static final int MAX_SENDS_PER_WINDOW = 3;
    static final long SEND_WINDOW_MILLIS = 60_000;
    static final int MAX_RECIPIENT_LENGTH = 254;

    final ConcurrentMap<String, long[]> sendWindows = new ConcurrentHashMap<>();

    private boolean allowSend(String recipient)
    {
        long now = System.currentTimeMillis();
        sendWindows.values().removeIf(w -> now - w[0] > SEND_WINDOW_MILLIS);
        long[] window = sendWindows.compute(recipient, (k, w) -> {
            if (w == null || now - w[0] > SEND_WINDOW_MILLIS)
                return new long[]{now, 1};
            w[1]++;
            return w;
        });
        return window[1] <= MAX_SENDS_PER_WINDOW;
    }


    ExpiredOTPProcessor expiredOTPProcessor ;
    public OTPManager()
    {
        expiredOTPProcessor = new ExpiredOTPProcessor(idotpexpiryMap);


    }

    @SneakyThrows
    public OTPResponse generateOTP(OTPRequest request) {

        String recipient = request == null ? null : request.getPhoneNumOrEmail();
        if (request == null || request.getType() == null || recipient == null
                || recipient.isBlank() || recipient.length() > MAX_RECIPIENT_LENGTH)
            throw new javax.ws.rs.BadRequestException("type and phoneNumOrEmail are required");

        if (!allowSend(recipient.trim().toLowerCase()))
            throw new javax.ws.rs.ClientErrorException("Too many OTP requests for this recipient", 429);

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

        long expiryTime = System.currentTimeMillis() + 1000*10;
        OTPResponse response = futureResponse.get();
        var idOtpExpiryTuple = new IdOTPTimeTuple(response.getMessageId(),otp,expiryTime );

        idotpexpiryMap.put(response.getMessageId(),idOtpExpiryTuple);

        expiredOTPProcessor.add(idOtpExpiryTuple);

        return response;

    }

    public OTPValidateResponse validateOTP(OTPValidate request) {

        OTPValidateResponse response = new OTPValidateResponse();
        response.setMessageId(request.getMessageId());
        var idOtpExpiryTuple = idotpexpiryMap.get(request.getMessageId());
        if (idOtpExpiryTuple!=null && request.getOTP()!=null)
        {
            if (idOtpExpiryTuple.getExpiryTime()<System.currentTimeMillis())
            {
                response.setResponse(OTPValidateResponse.Response.Expired);
                idotpexpiryMap.remove(request.getMessageId());
            }
            else
            {
                if (MessageDigest.isEqual(request.getOTP().getBytes(StandardCharsets.UTF_8),
                        idOtpExpiryTuple.getOTP().getBytes(StandardCharsets.UTF_8))) {
                    response.setResponse(OTPValidateResponse.Response.Valid);
                    idotpexpiryMap.remove(request.getMessageId());
                }
                else {
                    // the user can retry, but only a few times
                    response.setResponse(OTPValidateResponse.Response.Invalid);
                    if (idOtpExpiryTuple.incrementFailedAttempts() >= MAX_VALIDATION_ATTEMPTS)
                        idotpexpiryMap.remove(request.getMessageId());
                }
            }

        }
        else
        {
            response.setResponse(OTPValidateResponse.Response.Invalid);
        }

        return response;

    }
}
