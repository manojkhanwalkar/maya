package util.GVAPCHFUtil;

import java.util.Arrays;

public class DataRecord {


    char[] record;



    public DataRecord(char[] buffer) {

        this.record = buffer;
    }


    public void print()
    {
        for (char c: record)
        {
            System.out.print(c);
        }

        System.out.println();
    }
}
