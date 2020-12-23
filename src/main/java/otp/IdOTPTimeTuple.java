package otp;

import java.util.Objects;

public class IdOTPTimeTuple implements Comparable<IdOTPTimeTuple> {

    // expiry time hardcoded to 10 sec from current time .
    String id;
    String OTP;
    long expiryTime;

    public IdOTPTimeTuple(String id, String OTP) {
        this.id = id;
        this.OTP = OTP;
    }

    public IdOTPTimeTuple(String id, String OTP, long expiryTime) {
        this.id = id;
        this.OTP = OTP;
        this.expiryTime = expiryTime;
    }

    @Override
    public int compareTo(IdOTPTimeTuple other) {
        return Long.compare(this.getExpiryTime(),other.getExpiryTime() );
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    @Override
    public String toString() {
        return "IdOTPTimeTuple{" +
                "id='" + id + '\'' +
                ", OTP='" + OTP + '\'' +
                ", expiryTime=" + expiryTime +
                '}';
    }
}
