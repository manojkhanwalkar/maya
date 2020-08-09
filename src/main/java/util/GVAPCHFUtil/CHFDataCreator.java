package util.GVAPCHFUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static util.CardUtil.*;

public class CHFDataCreator {


    String dataFileName;

    int rows;

    public CHFDataCreator(String dataFileName, int rows)
    {
        this.dataFileName = dataFileName;
        this.rows = rows;
    }


    public void create()
    {

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(dataFileName)))
        {

            for (int i=0;i<rows;i++) {
                CHFDataRecord record = CHFDataRecord.create(createEin(), "FNAME"+i, "LNAME" +i);

                writer.write(record.getRecord());
                writer.newLine();

            }

            writer.flush();

        } catch(Exception ex)
        {
            ex.printStackTrace();
        }
//

    }

    public List<CHFDataRecord> read()
    {

        List<CHFDataRecord>  chfDataRecords = new ArrayList<>();

        try(BufferedReader reader= new BufferedReader(new FileReader(dataFileName)))
        {
            String line = reader.readLine();
            while(line!=null)
            {
               CHFDataRecord record = CHFDataRecord.create(line);
               chfDataRecords.add(record);

               line = reader.readLine();

            }

        }catch(Exception ex) {ex.printStackTrace(); }


        return chfDataRecords;

    }

    public static void main(String[] args) {


        CHFDataCreator dataCreator = new CHFDataCreator("/home/manoj/data/chf.txt" , 100);

  //      dataCreator.create();

     dataCreator.read().stream().forEach(System.out::println);

    }



}
