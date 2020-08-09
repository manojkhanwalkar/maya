package util.GVAPCHFUtil;

public class CHFGVAPDataRecord {

    int ein ;
    long tin;
    String ccF6L4;
    String fName;
    String lName;

    public CHFGVAPDataRecord(CHFDataRecord chfDataRecord , GVAPDataRecord gvapDataRecord)
    {
        if (chfDataRecord.getEin() != gvapDataRecord.getEin())
            throw new RuntimeException("ID does not maych " + chfDataRecord.getEin() + "  " + gvapDataRecord.getEin());


        ein = chfDataRecord.getEin();
        fName = chfDataRecord.getFName();
        lName = chfDataRecord.getLName();

        tin = gvapDataRecord.getTin();

        String tmp = gvapDataRecord.getPAN();

        ccF6L4 = tmp.substring(0,6) + tmp.substring(12,16);


    }

    @Override
    public String toString() {
        return "CHFGVAPDataRecord{" +
                "ein=" + ein +
                ", tin=" + tin +
                ", ccF6L4='" + ccF6L4 + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                '}';
    }
}
