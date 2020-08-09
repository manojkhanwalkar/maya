package util.GVAPCHFUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static util.CardUtil.*;

public class CHFDataCreator {


    String dataFileName;

    BufferedWriter writer;

    public CHFDataCreator(String dataFileName)
    {
        this.dataFileName = dataFileName;

        try {
            writer = new BufferedWriter(new FileWriter(dataFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void write(int ein, String fname , String lname)
    {


        try {
            CHFDataRecord record = CHFDataRecord.create(ein, fname, lname);

            writer.write(record.getRecord());
            writer.newLine();


            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
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




}
