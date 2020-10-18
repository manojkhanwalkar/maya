package cm;

import org.joda.time.LocalDate;

import java.time.LocalDateTime;

public class ClickRequest {

    String userId;

    String  time ;

    public ClickRequest(String userId) {
        this.userId = userId;

        time = LocalDateTime.now().toString();

    }


    public ClickRequest() {
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
