package util.GVAPCHFUtil;

import util.CardUtil;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CHFGVAPMerger {

    public static void main(String[] args) {


        CHFDataCreator chfDataCreator = new CHFDataCreator("/home/manoj/data/chf");

        GVAPDataCreator gvapDataCreator = new GVAPDataCreator("/home/manoj/data/gvap");

        for (int i=0;i<100;i++) {
            int ein1 = CardUtil.createEin();
            int ein2 = CardUtil.createEin();

            String card = CardUtil.createVCard();

            chfDataCreator.write(ein1, "FNAME " + i, "LNAME " + i);
            chfDataCreator.write(ein2, "FNAME " + i + i, "LNAME " + i);

            gvapDataCreator.write(ein1,card);
            gvapDataCreator.write(ein2,card);

        }

        Map<Integer,GVAPDataRecord>  pinGVAPRecord = new HashMap<>();

        var chf = chfDataCreator.read().stream().sorted(Comparator.comparingInt(CHFDataRecord::getEin)).collect(Collectors.toList());
        gvapDataCreator.read().stream().sorted(Comparator.comparingInt(GVAPDataRecord::getEin)).
                forEach(record->{pinGVAPRecord.put(record.getEin(),record);  });

        for (CHFDataRecord chfDataRecord : chf)
        {
            System.out.println(chfDataRecord + " " + pinGVAPRecord.get(chfDataRecord.getEin()));
        }

    }
}
