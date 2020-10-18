package cm;

import java.time.LocalDateTime;
import java.util.UUID;

public class OfferResponse {

    String userId;

    String  time ;

    String status ;

    String clickId ;

    public OfferResponse(String userId) {
        this.userId = userId;

        time = LocalDateTime.now().toString();

        status = "Approved";

        clickId = UUID.randomUUID().toString();
    }


    public OfferResponse()
    {

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClickId() {
        return clickId;
    }

    public void setClickId(String clickId) {
        this.clickId = clickId;
    }
}
