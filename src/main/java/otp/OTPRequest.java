package otp;

public class OTPRequest {

    public enum Type { Email , Phone} ;

    String phoneNumOrEmail;

    Type type ;

    public String getPhoneNumOrEmail() {
        return phoneNumOrEmail;
    }

    public void setPhoneNumOrEmail(String phoneNumOrEmail) {
        this.phoneNumOrEmail = phoneNumOrEmail;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
