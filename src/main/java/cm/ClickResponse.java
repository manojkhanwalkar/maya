package cm;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClickResponse {

    String userId;

    String  time ;

    String status ;

    String clickId ;

    public ClickResponse(String userId) {
        this.userId = userId;

        time = LocalDateTime.now().toString();

        status = "Approved";

        clickId = UUID.randomUUID().toString();
    }
}
