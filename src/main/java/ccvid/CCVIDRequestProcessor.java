package ccvid;

import common.Processor;
import data.*;
import pm.PMClient;
import util.ClientManager;
import util.JSONUtil;
import vfs.TransactionData;
import vfs.VFSClient;

import java.util.Date;
import java.util.UUID;

public class CCVIDRequestProcessor implements Processor<String,String> {


    @Override
    public String process(String payloadUnwrapped) {


        CCVIdResponse response = new CCVIdResponse();
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setResponseId(UUID.randomUUID().toString());

        try {

            CCVIdRequest request  = (CCVIdRequest) JSONUtil.fromJSON(payloadUnwrapped, CCVIdRequest.class);


            responseHeader.setRequestId(request.getRequestHeader().getRequestId());

            responseHeader.setStatus(ClientManager.status.Success.toString());

            responseHeader.setMessage("Processed request successfully");

            VelocityStatsList velocityStatsList = getVelocityStats(request.getAttributes().getCardNumber());

            response.setVelocityStatsList(velocityStatsList);


            PMResponse pmResponse = PMClient.submit(request.getAttributes());
            response.setScore(pmResponse.getScore());
            response.setResponseHeader(responseHeader);

            return JSONUtil.toJSON(response);




        } catch (Exception e) {
            e.printStackTrace();
        }



        return null ;



    }


    private static VelocityStatsList getVelocityStats(String cardNumber)
    {
        TransactionData transactionData = new TransactionData();


        transactionData.put("ccv",cardNumber);

        transactionData.setDate(new Date());


        VelocityStatsList velocityStats = VFSClient.submit(transactionData);

        return  velocityStats;

    }
}
