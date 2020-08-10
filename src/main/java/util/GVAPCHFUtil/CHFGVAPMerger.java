package util.GVAPCHFUtil;

import util.CardUtil;

import java.util.*;
import java.util.stream.Collectors;

import static util.CardUtil.createTin;

public class CHFGVAPMerger {

    public static void main(String[] args) {


        CHFDataCreator chfDataCreator = new CHFDataCreator("/home/manoj/data/chf");

        GVAPDataCreator gvapDataCreator = new GVAPDataCreator("/home/manoj/data/gvap");

        for (int i=0;i<100;i++) {
            int ein1 = CardUtil.createEin();
            int ein2 = CardUtil.createEin();

            long tin1= createTin();
            long tin2 = createTin();

            String card1 = CardUtil.createVCard();
            String cardM = CardUtil.createMCard();
            String cardF = CardUtil.createMCard();

            chfDataCreator.write(ein1, "FNAME" + i, "LNAME" + i);
            chfDataCreator.write(ein2, "FNAME" + i + i, "LNAME" + i);

            gvapDataCreator.write(ein1,tin1,card1);
            gvapDataCreator.write(ein1,tin1,cardM);
            gvapDataCreator.write(ein2,tin2,card1);
            gvapDataCreator.write(ein2,tin2,cardF);

        }

        Map<Integer,List<GVAPDataRecord>>  pinGVAPRecord = new HashMap<>();

        var chf = chfDataCreator.read().stream().sorted(Comparator.comparingInt(CHFDataRecord::getEin)).collect(Collectors.toList());

        gvapDataCreator.read().stream().sorted(Comparator.comparingInt(GVAPDataRecord::getEin)).
                forEach(record->{
                //    pinGVAPRecord.put(record.getEin(),record);
                   List<GVAPDataRecord> gvapDataRecords =  pinGVAPRecord.computeIfAbsent(record.getEin(),key->  new ArrayList<>());
                   gvapDataRecords.add(record);
                });


        List<CHFGVAPDataRecord> chfgvapDataRecords = new ArrayList<>();

        for (CHFDataRecord chfDataRecord : chf)
        {
           // System.out.println(chfDataRecord + " " + pinGVAPRecord.get(chfDataRecord.getEin()));

            chfgvapDataRecords.add(new CHFGVAPDataRecord(chfDataRecord,pinGVAPRecord.get(chfDataRecord.getEin())));


        }

    //    chfgvapDataRecords.stream().sorted(Comparator.comparingInt(CHFGVAPDataRecord::getEin)).forEach(System.out::println);
        chfgvapDataRecords.stream().sorted(Comparator.comparing(CHFGVAPDataRecord::getlName)).forEach(System.out::println);



        //TODO - create a new table in solr with the merged data record structure and query by CC number and FNAME and LName

    }
}
