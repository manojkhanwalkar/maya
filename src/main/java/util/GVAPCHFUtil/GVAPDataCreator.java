package util.GVAPCHFUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static util.CardUtil.*;

public class GVAPDataCreator {


    String dataFileName;


    BufferedWriter writer;

    public GVAPDataCreator(String dataFileName)
    {
        this.dataFileName = dataFileName;
        try {
            writer = new BufferedWriter(new FileWriter(dataFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void write(int ein, long tin, String card)
    {

        try {
            GVAPDataRecord record = GVAPDataRecord.create(ein, tin, card);

            writer.write(record.getRecord());
            writer.newLine();

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public List<GVAPDataRecord> read()
    {

        List<GVAPDataRecord>  gvapDataRecords = new ArrayList<>();

        try(BufferedReader reader= new BufferedReader(new FileReader(dataFileName)))
        {
            String line = reader.readLine();
            while(line!=null)
            {
               GVAPDataRecord record = GVAPDataRecord.create(line);
               gvapDataRecords.add(record);

               line = reader.readLine();

            }

        }catch(Exception ex) {ex.printStackTrace(); }


        return gvapDataRecords;

    }



}
