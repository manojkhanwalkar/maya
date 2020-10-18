package cm;

import java.time.LocalDateTime;

public class OfferRequest {

    String userId;

    String  time ;

    public OfferRequest(String userId) {
        this.userId = userId;

        time = LocalDateTime.now().toString();

    }


    public OfferRequest() {
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
