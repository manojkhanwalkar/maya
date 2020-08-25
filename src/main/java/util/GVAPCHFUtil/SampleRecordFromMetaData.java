package util.GVAPCHFUtil;

import java.util.Random;

public class SampleRecordFromMetaData {


    public static DataRecord create(CHFMetaData chfMetaData)
    {
        int length = chfMetaData.chfFields.get(chfMetaData.chfFields.size()-1).end;

        char[] buffer = new char[length+1];
        for (int i=0;i<buffer.length;i++)
        {
            buffer[i] = ' ';
        }

        for (CHFMetaData.Field field : chfMetaData.chfFields)
        {
            if (field.type.equals("Character"))
                StringFiller(buffer,field.len,field.start);
            else if (field.type.equals("Numeric"))
                NumberFiller(buffer,field.len,field.start);
            else
            {
                System.out.println(field);
            }
        }

        return new DataRecord(buffer);


    }


    static Random random = new Random();
    private static void StringFiller(char[]buffer , int length, int start)
    {
        int max = random.nextInt(length+1);
        for (int i=0;i<max;i++)
        {
            buffer[start+i] = (char) ('a' + random.nextInt(26));
        }
    }

    private static void NumberFiller(char[]buffer, int length, int start)
    {
        int max = random.nextInt(length+1);
        for (int i=0;i<max;i++)
        {
            buffer[start+i] = (char) ('0' + random.nextInt(10));
        }

    }


    public static void main(String[] args) {

        DataRecord dataRecord = create(CHFMetaData.parse("/home/manoj/data/CHF.txt"));

        dataRecord.print();

    }

}
