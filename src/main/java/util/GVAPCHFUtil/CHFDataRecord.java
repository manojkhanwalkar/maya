package util.GVAPCHFUtil;

public class CHFDataRecord {

    static final int RecordLength = 59;


    char[] dataRecord = new char[RecordLength];

    public static CHFDataRecord create(int ein , String fname, String lname)
    {
        CHFDataRecord dataRecord = new CHFDataRecord();

        dataRecord.setEin(ein);

        dataRecord.setFName(fname);

        dataRecord.setLName(lname);

        return dataRecord;

    }

    public static CHFDataRecord create(String line)
    {
        CHFDataRecord dataRecord = new CHFDataRecord();

        dataRecord.dataRecord = line.toCharArray();

        return dataRecord;

    }

    private CHFDataRecord()
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



    // fname 10-29

    public void setFName(String fname)
    {
        for (int i=10, j=0;i<10+fname.length();i++,j++)
        {
            dataRecord[i] = fname.charAt(j);
        }

    }

    public String getFName()
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

        return builder.toString();

    }
    // lname - 30 -59


    public void setLName(String lname)
    {
        for (int i=30, j=0;i<30+lname.length();i++,j++)
        {
            dataRecord[i] = lname.charAt(j);
        }

    }

    public String getLName()
    {
        StringBuilder builder = new StringBuilder();
        for (int i=30;i<59;i++)
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
