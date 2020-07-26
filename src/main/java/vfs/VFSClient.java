package vfs;

import data.VelocityStatsList;
import util.Connection;
import util.JSONUtil;

import java.util.Date;

public class VFSClient {

   /* public static void main(String[] args) {

        CCVIPValues.init("ccv","ip");

        for (int i=0;i<1;i++) {

            TransactionData transactionData = new TransactionData();

            String ccvValue = CCVIPValues.get("ccv");

            transactionData.put("ccv",ccvValue);
            transactionData.put("ip",CCVIPValues.get("ip"));

            transactionData.setDate(new Date());


            VelocityStatsList velocityStats = submit(transactionData);

            //System.out.println(ccvValue + " ==> " + velocityStats);

        }

    }*/


    public static VelocityStatsList submit(TransactionData transactionData)
    {
        // send to scheduler a jar file and client name and get back a job id .

        // System.out.println(profileId);

        Connection app = new Connection("https://localhost:8880/");

        String result = app.sendSimple(JSONUtil.toJSON(transactionData),"transaction");

        System.out.println(result);

        return (VelocityStatsList) JSONUtil.fromJSON(result,VelocityStatsList.class);

    }


}
