package otp;

import java.util.Objects;

public class IdOTPTimeTuple implements Comparable<IdOTPTimeTuple> {

    // expiry time hardcoded to 10 sec from current time .
    String id;
    String OTP;
    long expiryTime;

    @Override
    public int compareTo(IdOTPTimeTuple other) {
        return Long.compare(this.getExpiryTime(),other.getExpiryTime() );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdOTPTimeTuple that = (IdOTPTimeTuple) o;
        return id.equals(that.id) && OTP.equals(that.OTP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, OTP);
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
}
