package client;

import data.NonceRequest;
import data.NonceResponse;
import util.Sender;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class NonceCache {


    final int capacity ;

    final int waterMark;

    public NonceCache(int capacity)
    {
        this.capacity = capacity;

        waterMark = capacity/2;
    }

    LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    AtomicBoolean gate = new AtomicBoolean(false);

    public String getNonce()
    {
        // blocks if nonce is not available

        try {

            if (queue.size()<=waterMark && gate.compareAndSet(false,true))
            {
                CompletableFuture.runAsync(()->{populateNonceCache();});
            }

            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    Sender sender = new Sender("https://localhost:8180/");

    private void populateNonceCache()
    {

        while(queue.size()<=capacity) {
            NonceRequest nonceRequest = new NonceRequest();
            nonceRequest.setNumber(2);

            NonceResponse nonceResponse = sender.send(nonceRequest, "nonce");

            for (String str : nonceResponse.getNonces()) {
                try {
                    queue.put(str);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        gate.set(false);



    }


}
