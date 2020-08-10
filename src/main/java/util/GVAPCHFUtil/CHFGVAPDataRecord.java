package util.GVAPCHFUtil;

import java.util.ArrayList;
import java.util.List;

public class CHFGVAPDataRecord {

    int ein ;
    long tin;
    List<String> ccF6L4List = new ArrayList<>();
    String fName;
    String lName;


    public int getEin() {
        return ein;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public CHFGVAPDataRecord(CHFDataRecord chfDataRecord , List<GVAPDataRecord> gvapDataRecords)
    {
        if (chfDataRecord.getEin() != gvapDataRecords.get(0).getEin())
            throw new RuntimeException("ID does not maych " + chfDataRecord.getEin() + "  " + gvapDataRecords.get(0).getEin());


        ein = chfDataRecord.getEin();
        fName = chfDataRecord.getFName();
        lName = chfDataRecord.getLName();

        tin = gvapDataRecords.get(0).getTin();

        gvapDataRecords.forEach(gvapDataRecord -> {

            String tmp = gvapDataRecord.getPAN();
            ccF6L4List.add(tmp.substring(0,6) + tmp.substring(12,16));
        });

    }

    @Override
    public String toString() {
        return "CHFGVAPDataRecord{" +
                "ein=" + ein +
                ", tin=" + tin +
                ", ccF6L4='" + ccF6L4List + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                '}';
    }
}
