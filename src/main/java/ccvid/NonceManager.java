package ccvid;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

public class NonceManager {

    // Bounds so one unauthenticated request cannot exhaust memory or CPU.
    static final int MAX_NONCES_PER_REQUEST = 100;
    static final int MAX_STORED_NONCES = 100_000;

    ConcurrentSkipListSet<String> nonces = new ConcurrentSkipListSet<>();


    public ArrayList<String> getNonces(int num)
    {
        if (num < 1 || num > MAX_NONCES_PER_REQUEST)
            throw new javax.ws.rs.BadRequestException("number must be between 1 and " + MAX_NONCES_PER_REQUEST);
        if (nonces.size() + num > MAX_STORED_NONCES)
            throw new javax.ws.rs.ServiceUnavailableException("nonce store is full");

        ArrayList<String> results = new ArrayList<>();
        for (int i=0;i<num;i++)
        {
            String str = UUID.randomUUID().toString();

            results.add(str);

            nonces.add(str);


        }

        return results;

    }


}
