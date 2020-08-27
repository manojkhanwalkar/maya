package util;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TroveMapWrapper<T> {

    int stripes = 16;  int capacity = 1024;
    TIntObjectHashMap<T> [] maps;
    Lock[] locks ;

    public TroveMapWrapper(int stripes, int capacity) {
        this.stripes = stripes;
        this.capacity = capacity;


        init();
    }

    public TroveMapWrapper() {

        init();
    }



    AtomicInteger size = new AtomicInteger(0);

    private void init()
    {
        maps = new TIntObjectHashMap[stripes];

        locks = new ReentrantLock[stripes];

        for (int i=0;i<stripes;i++)
        {
            locks[i] = new ReentrantLock();

            maps[i] = new TIntObjectHashMap<>(capacity);
        }

    }


    public void put(int key, T value)
    {
        int bucket = key%stripes;

        locks[bucket].lock();

        maps[bucket].put(key,value);

        locks[bucket].unlock();

        size.getAndIncrement();

    }

    public T get(int key)
    {
        int bucket = key%stripes;

        try {
            locks[bucket].lock();
                return maps[bucket].get(key);
        } finally {
            locks[bucket].unlock();

        }

    }


    public int size()
    {
        return size.get();
    }



}
