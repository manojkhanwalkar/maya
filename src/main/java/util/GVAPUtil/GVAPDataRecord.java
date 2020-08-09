package util.GVAPUtil;

import org.bouncycastle.jcajce.provider.symmetric.GOST28147;

public class GVAPDataRecord {

    static final int RecordLength = 69;


    char[] dataRecord = new char[RecordLength];

    public static GVAPDataRecord create(int ein , long tin , String pan)
    {
        GVAPDataRecord dataRecord = new GVAPDataRecord();

        dataRecord.setEin(ein);

        dataRecord.setTin(tin);

        dataRecord.setPAN(pan);

        return dataRecord;

    }

    public static GVAPDataRecord create(String line)
    {
        GVAPDataRecord dataRecord = new GVAPDataRecord();

        dataRecord.dataRecord = line.toCharArray();

        return dataRecord;

    }

    private  GVAPDataRecord()
    {
        init();
    }

    private void  init()
    {
        for (int i=0;i<RecordLength;i++)
        {
            dataRecord[i] = ' ';
        }
    }

    // ein 0-9
    // tin 10-29
    // pan - 30 -69

    public void setEin(int ein)
    {
        String str = String.valueOf(ein);

        for (int i=0;i<str.length();i++)
        {
            dataRecord[i] = str.charAt(i);
        }
    }

    public int getEin()
    {
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<10;i++)
        {
            if (dataRecord[i]==' ')
                break;
            else
            {
                builder.append(dataRecord[i]);
            }
        }

        Integer ein = Integer.valueOf(builder.toString());
        return ein;

    }

   public void setTin(long tin)
    {
        String str = String.valueOf(tin);

        for (int i=10, j=0;i<10+str.length();i++,j++)
        {
            dataRecord[i] = str.charAt(j);
        }
    }

    public long getTin()
    {
        StringBuilder builder = new StringBuilder();
        for (int i=10;i<29;i++)
        {
            if (dataRecord[i]==' ')
                break;
            else
            {
                builder.append(dataRecord[i]);
            }
        }

        Long tin = Long.valueOf(builder.toString());
        return tin;
    }

   public void setPAN(String ccv)
    {
        for (int i=30, j=0;i<30+ccv.length();i++,j++)
        {
            dataRecord[i] = ccv.charAt(j);
        }

    }

    public String getPAN()
    {
        StringBuilder builder = new StringBuilder();
        for (int i=30;i<69;i++)
        {
            if (dataRecord[i]==' ')
                break;
            else
            {
                builder.append(dataRecord[i]);
            }
        }

        return builder.toString();

    }

 public void print()
 {
     for (int i=0;i<RecordLength;i++)
     {
         System.out.print(dataRecord[i]);
     }
 }

 public String toString()
 {
     return getRecord();
 }

 public String getRecord()
 {
     StringBuilder builder = new StringBuilder(dataRecord.length);
     builder.append(dataRecord);

     return  builder.toString();
 }

}
