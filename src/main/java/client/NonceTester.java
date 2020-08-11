package client;

import data.NonceRequest;
import data.NonceResponse;
import util.Sender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NonceTester {

    public static void main(String[] args) throws Exception {


        ExecutorService service = Executors.newFixedThreadPool(10);


        NonceCache nonceCache = new NonceCache(10);


        Runnable noncer = ()-> {

            for (int i = 0; i < 100; i++) {
                String str = nonceCache.getNonce();

                System.out.println(Thread.currentThread().getName() + "  " + i + "      " + str);
            }

        };

        for (int i=0;i<10;i++)
        {
            service.submit(noncer);
        }


        service.shutdown();



    }
}
