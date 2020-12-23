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

    public void add(IdOTPTimeTuple tuple)
    {
        queue.add(tuple);
    }

    public void cleanup()
    {
        long curr = System.currentTimeMillis();
        while(!queue.isEmpty())
        {
            if (queue.peek().getExpiryTime() < curr) {
                System.out.println("Removing expired otp " + queue.remove());

            }
            else
                break;
        }
    }
}
