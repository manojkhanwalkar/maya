package util.GVAPCHFUtil;

import util.CardUtil;

public class CHFGVAPMerger {

    public static void main(String[] args) {


        CHFDataCreator chfDataCreator = new CHFDataCreator("/home/manoj/data/chf");

        GVAPDataCreator gvapDataCreator = new GVAPDataCreator("/home/manoj/data/gvap");

   /*     for (int i=0;i<100;i++) {
            int ein1 = CardUtil.createEin();
            int ein2 = CardUtil.createEin();

            String card = CardUtil.createVCard();

            chfDataCreator.write(ein1, "FNAME " + i, "LNAME " + i);
            chfDataCreator.write(ein2, "FNAME " + i + i, "LNAME " + i);

            gvapDataCreator.write(ein1,card);
            gvapDataCreator.write(ein2,card);

        }*/



    }
}
