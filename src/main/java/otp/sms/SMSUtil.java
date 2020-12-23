package otp.sms;

import java.util.concurrent.ThreadLocalRandom;

public class SMSUtil {

    static ThreadLocalRandom random = ThreadLocalRandom.current();


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
