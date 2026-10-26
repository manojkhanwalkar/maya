package otp.sms;

import java.security.SecureRandom;

public class SMSUtil {

    // OTPs must be unpredictable: use a CSPRNG.
    static final SecureRandom random = new SecureRandom();


    public static String OTPString()
    {

        StringBuilder builder = new StringBuilder(6);
        for (int i=0;i<6;i++)
        {
            builder.append(random.nextInt(10));
        }

        return builder.toString();
    }
}
