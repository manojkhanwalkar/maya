package util.GVAPCHFUtil;

import data.CcvPin;
import data.PinCCVPii;
import data.PinPii;
import util.SolrClient;
import util.SolrUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class CHFGVAPSolrMerger {

    public static void main(String[] args) throws Exception {

        SolrUtil.getInstance().addClient("http://localhost:8983/solr/","ccvpin");
        SolrUtil.getInstance().addClient("http://localhost:8983/solr/","pinccvpii");

        SolrClient<CcvPin> gvap  = SolrUtil.getInstance().getClient("ccvpin");


        SolrClient<PinCCVPii> merge  = SolrUtil.getInstance().getClient("pinccvpii");

        //5040883693945693


        List<PinCCVPii> list1 = merge.query("firstName:MrFNAME~2" ,PinCCVPii.class);

        System.out.println(list1);


    /*    CHFDataCreator chfDataCreator = new CHFDataCreator("/home/manoj/data//chfgvap/chf" , "");


        chfDataCreator.read().stream().forEach(chfRecord->{

            List<CcvPin> list1 = gvap.query("ein:"+chfRecord.getEin(),CcvPin.class);

            for (CcvPin ccvPin : list1) {

                PinCCVPii bean = new PinCCVPii();
                bean.setCcv(ccvPin.getCcv());
                bean.setEin(chfRecord.getEin());
                bean.setFName(chfRecord.getFName());
                bean.setLName(chfRecord.getLName());

                merge.add(bean);
            }

        });

        merge.commit();*/


    /*    GVAPDataCreator gvapDataCreator = new GVAPDataCreator("/home/manoj/data/chfgvap/gvap" , "");


        var iter = gvapDataCreator.iterator();

        while (iter.hasNext()) {
            GVAPDataRecord record = iter.next();
            CcvPin cp = new CcvPin();
            cp.setCcv(record.getPAN());
            cp.setEin(record.getEin());
            gvap.add(cp);

        }

        gvap.commit();*/


/*        final int count = 1000000;


        for (int j=0;j<5;j++) {
            CHFDataCreator chfDataCreator = new CHFDataCreator("/home/manoj/data//chfgvap/chf"+j );


            GVAPDataCreator gvapDataCreator = new GVAPDataCreator(+j);


            for (int i = 0; i < count; i++) {
                int ein1 = CardUtil.createEin();
                int ein2 = CardUtil.createEin();

                long tin1 = createTin();
                long tin2 = createTin();

                String card1 = CardUtil.createVCard();
                String cardM = CardUtil.createMCard();
                String cardF = CardUtil.createMCard();
                String cardMM = CardUtil.createMCard();
                String cardFF = CardUtil.createMCard();
                String cardMMM = CardUtil.createMCard();
                String cardFFF = CardUtil.createMCard();

                chfDataCreator.write(ein1, "MrFNAME" + i, "LNAME" + i);
                chfDataCreator.write(ein2, "MRSFNAME" + i, "LNAME" + i);

                gvapDataCreator.write(ein1, tin1, card1);
                gvapDataCreator.write(ein1, tin1, cardM);
                gvapDataCreator.write(ein1, tin1, cardMM);
                gvapDataCreator.write(ein1, tin1, cardMMM);
                gvapDataCreator.write(ein2, tin2, card1);
                gvapDataCreator.write(ein2, tin2, cardF);
                gvapDataCreator.write(ein2, tin2, cardFF);
                gvapDataCreator.write(ein2, tin2, cardFFF);

            }


        }*/

/*
        {
            CompletableFuture<Void>[] cfs = new CompletableFuture[5];
            for (int j = 0; j < 5; j++) {

                final int index = j;

                cfs[j] = CompletableFuture.runAsync(() -> {
                    CHFDataCreator chfDataCreator = new CHFDataCreator("/home/manoj/data//chfgvap/chf" + index, "");


                     chfDataCreator.readIntoMap();


                });

            }

            CompletableFuture.allOf(cfs).join();



        }


        System.out.println("CHF size " + (CHFDataCreator.chfDataRecords.size()));


        var chfMap = CHFDataCreator.chfDataRecords;

        AtomicInteger matchCount = new AtomicInteger(0);

        Function<String,Runnable> func = (fileName) -> {

            Runnable runnable = ()-> {

                    CHFInMemDataRecord chfInMemDataRecord = chfMap.get(record.getEin());
                    if (chfInMemDataRecord != null) {
                        matchCount.incrementAndGet();
                    }
                }


            };

         return runnable;

        };


        CompletableFuture<Void>[] cfs = new CompletableFuture[5];

        for (int j=0;j<5;j++) {

            cfs[j]= CompletableFuture.runAsync(func.apply("/home/manoj/data/chfgvap/gvap"+j));



        }

        CompletableFuture.allOf(cfs).join();

        System.out.println("Matched records " + matchCount);*/

    }
}
