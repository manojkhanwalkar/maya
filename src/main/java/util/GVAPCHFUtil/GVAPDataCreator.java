package util.GVAPCHFUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
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


    public GVAPDataCreator(String dataFileName, String mode)
    {
        this.dataFileName = dataFileName;


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


    public Iterator<GVAPDataRecord> iterator()
    {
            return new GVAPIterator(dataFileName);
    }


    static class GVAPIterator implements Iterator<GVAPDataRecord> {
        BufferedReader reader;

        public GVAPIterator(String dataFileName) {
            try {
                reader = new BufferedReader(new FileReader(dataFileName));
                line = reader.readLine();
                if (line == null) more = false;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean more = true;
        String line;

        @Override
        public boolean hasNext() {
            return more;
        }

        @Override
        public GVAPDataRecord next() {
            GVAPDataRecord record = GVAPDataRecord.create(line);
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) more = false;
            return record;
        }

    }

}
