package otp.sms;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static otp.sms.SMSUtil.OTPString;

public class PhoneSMS {

    final AmazonSNS snsClient;
    final Map<String, MessageAttributeValue> smsAttributes;
    public PhoneSMS() {
        snsClient  = AmazonSNSClientBuilder.standard()
                .withRegion(Regions.US_EAST_1).build();

        smsAttributes= new HashMap<String, MessageAttributeValue>();
        //smsAttributes.put("AWS.SNS.SMS.SenderID",new MessageAttributeValue().withStringValue("SENDER-ID").withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.SMSType",new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));
    }

    public String send(String phoneNum, String otp)
    {
        PublishRequest request = new PublishRequest();
        request.withMessage("Your 6 digit OTP code is " + otp)
                .withPhoneNumber(phoneNum)
                .withMessageAttributes(smsAttributes);
        PublishResult result=snsClient.publish(request);

        return result.getMessageId();

    }

    public static void main(String[] args) {

        PhoneSMS phoneSMS = new PhoneSMS();

        String id = phoneSMS.send("+17327620499", OTPString());

        System.out.println(id);
    }




}
