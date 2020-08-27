package util.GVAPCHFUtil;

import gnu.trove.map.hash.TIntObjectHashMap;
import util.TroveMapWrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

    public CHFDataCreator(String dataFileName, String mode)
    {
        this.dataFileName = dataFileName;

       // dummy param to make it read only
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


   static TroveMapWrapper<CHFInMemDataRecord> chfDataRecords = new TroveMapWrapper<>(5,10240001/5);


    public  void readIntoMap()
    {

        //TIntObjectHashMap<CHFInMemDataRecord> chfDataRecords = new TIntObjectHashMap<>(1024001);


        try(BufferedReader reader= new BufferedReader(new FileReader(dataFileName)))
        {
            String line = reader.readLine();
            while(line!=null)
            {
                CHFInMemDataRecord record = CHFInMemDataRecord.create(line);

                chfDataRecords.put(record.ein, record);


                line = reader.readLine();

            }

        }catch(Exception ex) {ex.printStackTrace(); }




    }



}
