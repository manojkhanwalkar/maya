import data.CcvPin;
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

    public static void main(String[] args)  throws Exception {

        SolrUtil.getInstance().addClient("http://localhost:8983/solr/","ccvpin");
        SolrUtil.getInstance().addClient("http://localhost:8983/solr/","pinpii");


        SolrClient<CcvPin> solrClient1  = SolrUtil.getInstance().getClient("ccvpin");

        SolrClient<PinPii> solrClient2  = SolrUtil.getInstance().getClient("pinpii");



        ThreadLocalRandom random = ThreadLocalRandom.current();

       CcvPin cp = new CcvPin();
        cp.setCcv("C1");
        cp.setEin(random.nextInt(Integer.MAX_VALUE));
        cp.setTin(random.nextInt(Integer.MAX_VALUE));

        solrClient1.add(cp);

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
            pp.setTin(cp1.getTin());

            pp.setFName("FN"+count);
            pp.setLName("LN" + count);

            solrClient2.add(pp);

        }

        solrClient2.commit();

        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

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





    }


}
