import data.CcvPin;
import data.PinCCVPii;
import data.PinPii;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import util.JSONUtil;
import util.SolrClient;
import util.SolrUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SolrTest {

    //solr-8.6.0:$ ./bin/solr start -e cloud

    public static void main(String[] args)  throws Exception {

        SolrUtil.getInstance().addClient("http://localhost:8983/solr/","ccvpin");
        SolrUtil.getInstance().addClient("http://localhost:8983/solr/","pinpii");
        SolrUtil.getInstance().addClient("http://localhost:8983/solr/","pinccvpii");


        SolrClient<CcvPin> solrClient1  = SolrUtil.getInstance().getClient("ccvpin");

        SolrClient<PinPii> solrClient2  = SolrUtil.getInstance().getClient("pinpii");

        SolrClient<PinCCVPii> pinccvpii  = SolrUtil.getInstance().getClient("pinccvpii");


        ThreadLocalRandom random = ThreadLocalRandom.current();

     /*

        cp.setCcv("C1");
        cp.setEin(random.nextInt(Integer.MAX_VALUE));
        cp.setTin(random.nextInt(Integer.MAX_VALUE));

        solrClient1.add(cp);

        cp.setCcv("C2");
        cp.setEin(random.nextInt(Integer.MAX_VALUE));
        cp.setTin(random.nextInt(Integer.MAX_VALUE));

        solrClient1.add(cp);



        cp.setCcv("C2");
        cp.setEin(random.nextInt(Integer.MAX_VALUE));
        cp.setTin(random.nextInt(Integer.MAX_VALUE));

        solrClient1.add(cp);

        solrClient1.commit();

        List<CcvPin> list = solrClient1.query("ccv:C1",CcvPin.class);


        System.out.println(list);


        int count = random.nextInt(Integer.MAX_VALUE/2);
       for (CcvPin cp1 : list) {

            PinPii pp = new PinPii();
            pp.setEin(cp1.getEin());


            pp.setFName("FN"+count);
            pp.setLName("LN" + count);

            solrClient2.add(pp);

        }

        solrClient2.commit();*/

        int ein = random.nextInt(Integer.MAX_VALUE);

        {
            CcvPin cp = new CcvPin();
            cp.setCcv("4000500060007000");


            cp.setEin(ein);
            cp.setTin(random.nextInt(Integer.MAX_VALUE));

            solrClient1.add(cp);

        }

        {
            CcvPin cp = new CcvPin();
            cp.setCcv("5000500060007000");


            cp.setEin(ein);
            cp.setTin(random.nextInt(Integer.MAX_VALUE));

            solrClient1.add(cp);
        }

        solrClient1.commit();


        // merge

        List<CcvPin> list1 = solrClient1.query("ein:"+ein,CcvPin.class);

        System.out.println(list1);

        for (CcvPin ccvPin : list1) {

            PinCCVPii bean = new PinCCVPii();
            bean.setCcv(ccvPin.getCcv());
            bean.setEin(ein);
            bean.setFName("FName");
            bean.setLName("LName");

            pinccvpii.add(bean);
        }
       pinccvpii.commit();

    /*    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

        {
            List<PinPii> list1 = solrClient2.query("id:1991845417",PinPii.class);

            System.out.println(list1);

        }


        System.out.println("++++++++++++++++++++++++++++");

        {
            final Map<String, String> queryParamMap = new HashMap<String, String>();
            queryParamMap.put("q", "*:*");
            queryParamMap.put("fl", "id,tin,firstName,lastName");
           // queryParamMap.put("sort", "ein desc");

            List<PinPii> listAll = solrClient2.query(queryParamMap,PinPii.class);

            System.out.println(listAll);


        }
*/




    }


}
