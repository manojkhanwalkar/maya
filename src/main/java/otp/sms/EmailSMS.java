package otp.sms;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import lombok.SneakyThrows;

import java.io.IOException;

import static otp.sms.SMSUtil.OTPString;


public class EmailSMS {

    final AmazonSimpleEmailService client;
    public EmailSMS() {
        client = AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(Regions.US_EAST_1).build();

    }

    @SneakyThrows
    public String send(String to)
    {
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(to))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData("Your OTP is" + OTPString())))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData("OTP Test")))
                    .withSource("manojkhanwalkar@gmail.com");

            SendEmailResult result = client.sendEmail(request);

            return result.getMessageId();

    }

}
