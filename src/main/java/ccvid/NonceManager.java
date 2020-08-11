package ccvid;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

public class NonceManager {

    ConcurrentSkipListSet<String> nonces = new ConcurrentSkipListSet<>();


    public ArrayList<String> getNonces(int num)
    {
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
