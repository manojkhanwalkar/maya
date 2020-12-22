package otp.pm;

public class OTPValidateResponse {

    public enum Response { Valid , Invalid , Expired};
    String messageId;
    Response response;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
