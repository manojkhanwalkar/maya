package util.GVAPCHFUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static util.CardUtil.*;

public class GVAPDataCreator {


    String dataFileName;

    int rows;

    public GVAPDataCreator(String dataFileName, int rows)
    {
        this.dataFileName = dataFileName;
        this.rows = rows;
    }


    public void create()
    {

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(dataFileName)))
        {

            for (int i=0;i<rows;i++) {
                GVAPDataRecord record = GVAPDataRecord.create(createEin(), createTin(), createVCard());

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

    public static void main(String[] args) {


        GVAPDataCreator dataCreator = new GVAPDataCreator("/home/manoj/data/gvap.txt" , 100);

       // dataCreator.create();

        dataCreator.read().stream().forEach(System.out::println);

    }



}
