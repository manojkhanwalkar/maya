package vfs;

import data.VelocityStats;
import data.VelocityStatsList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VFSManager {

    Map<String,VelocityStatsManager> attrVelocityMap = new HashMap<>();

    String[] attributes = { "ccv" , "ip"};

    transient ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);

    public void init() {

        CCVIPValues.init(attributes);

        Arrays.stream(attributes).forEach(attr->{

            attrVelocityMap.put(attr, new VelocityStatsManager(attr));
        });

        CurrentCountRecycler recycler = new CurrentCountRecycler(this);
        service.scheduleAtFixedRate(recycler,15,15, TimeUnit.SECONDS);

        fileManager.recover();
    }


    static class CurrentCountRecycler implements Runnable
    {

        final VFSManager vfsManager;

        public CurrentCountRecycler(VFSManager vfsManager) {
            this.vfsManager = vfsManager;
        }

        @Override
        public void run() {

            Map<String,VelocityStatsManager> attrVelocityMap = vfsManager.attrVelocityMap;

            attrVelocityMap.values().stream().forEach(vsm->{

                vsm.currentCountMap.values().stream().forEach(currentCount -> {
                    synchronized (currentCount)
                    {

                        int count = currentCount.minuteCount.removeFirst().count;
                        currentCount.prevCount += count;
                        currentCount.minuteCount.add(new CurrentCount.Counter());
                    }

                });

            });



        }
    }


    CurrentFileManager fileManager = new CurrentFileManager(this,"ccv", "ip");



    public VelocityStatsList process(TransactionData transactionData)
    {

        VelocityStatsList velocityStatsList = new VelocityStatsList();

        if (transactionData == null || transactionData.getAttrValueMap() == null)
            return velocityStatsList;

        transactionData.getAttrValueMap().entrySet().stream().forEach(entry->{

            String key = entry.getKey();
            String value = entry.getValue();

            // Only the configured attributes are tracked, and values are written one per line to
            // the day files: reject unknown attributes and values that could inject extra lines
            // or grow the in-memory counters without bound.
            if (!isAcceptable(key, value))
                return;

            fileManager.write(key,value);

            VelocityStatsManager velocityStatsManager = attrVelocityMap.get(key);
            if (velocityStatsManager!=null)
            {
                VelocityStats velocityStats = velocityStatsManager.add(value);
                velocityStats.setType(key);
                velocityStats.setValue(value);
                velocityStatsList.addStats(velocityStats);
            }
        });



        return velocityStatsList;

    }

    static final int MAX_VALUE_LENGTH = 64;

    private boolean isAcceptable(String key, String value)
    {
        return key != null && Arrays.asList(attributes).contains(key)
                && value != null && !value.isEmpty() && value.length() <= MAX_VALUE_LENGTH
                && value.chars().noneMatch(Character::isISOControl);
    }

    public static void main(String[] args) {

        VFSManager vfsManager = new VFSManager();

        vfsManager.init();

      //  System.out.println(vfsManager.attrVelocityMap);

    }
}
