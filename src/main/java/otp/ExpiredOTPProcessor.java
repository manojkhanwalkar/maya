package otp;

import java.util.PriorityQueue;
import java.util.concurrent.*;

public class ExpiredOTPProcessor {

    PriorityQueue<IdOTPTimeTuple> queue = new PriorityQueue<>();
    ConcurrentMap<String, IdOTPTimeTuple> idotpexpiryMap;

    ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

    public ExpiredOTPProcessor(ConcurrentMap<String, IdOTPTimeTuple> idotpexpiryMap) {

        this.idotpexpiryMap = idotpexpiryMap;

        ses.scheduleAtFixedRate(this::cleanup, 5, 5, TimeUnit.SECONDS);

    }

    public synchronized void add(IdOTPTimeTuple tuple)
    {
        queue.add(tuple);
    }

    public synchronized void cleanup()
    {
        long curr = System.currentTimeMillis();
        while(!queue.isEmpty())
        {
            if (queue.peek().getExpiryTime() < curr) {
                // drop it from the lookup map too, otherwise unused OTPs accumulate forever
                IdOTPTimeTuple expired = queue.remove();
                idotpexpiryMap.remove(expired.getId(), expired);

            }
            else
                break;
        }
    }
}
