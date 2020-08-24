package util.GVAPCHFUtil;

public class CHFInMemDataRecord {


    Integer ein;
    String fname;
    String lname;




    public static CHFInMemDataRecord create(String line)
    {
        CHFInMemDataRecord dataRecord = new CHFInMemDataRecord();


        char[] arr = line.toCharArray();

        dataRecord.ein = getEin(arr);
        dataRecord.fname = getFName(arr);
        dataRecord.lname = getLName(arr);


        return dataRecord;

    }





    public static int getEin(char[] dataRecord)
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





    public static String getFName(char[] dataRecord)
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




    public static String getLName(char[] dataRecord)
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



}
