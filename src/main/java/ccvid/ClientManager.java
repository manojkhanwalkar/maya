package ccvid;

import com.nimbusds.jose.JWEObject;
import data.*;
import pm.PMClient;
import util.JSONUtil;
import vfs.TransactionData;
import vfs.VFSClient;

import java.util.Date;
import java.util.UUID;

public class ClientManager {

    public enum status { Success, Error}

    public static EncryptedSignedResponse process(EncryptedSignedRequest encryptedSignedRequest, ClientCache clientCache)
    {
        CCVIdResponse response = new CCVIdResponse();
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setResponseId(UUID.randomUUID().toString());

        try {
            String payload = encryptedSignedRequest.getEncryptedSignedPayload();

            var jweObject = JWEObject.parse(payload);

            String keyId = jweObject.getHeader().getKeyID();

            ClientHandler clientHandler = clientCache.get(keyId).orElseThrow();

            CCVIdRequest request = clientHandler.unwrap(payload);

            responseHeader.setRequestId(request.getRequestHeader().getRequestId());

            responseHeader.setStatus(status.Success.toString());

            responseHeader.setMessage("Processed request successfully");

            VelocityStatsList velocityStatsList = getVelocityStats(request.getAttributes().getCardNumber());

            response.setVelocityStatsList(velocityStatsList);


            PMResponse pmResponse = PMClient.submit(request.getAttributes());
            response.setScore(pmResponse.getScore());
            response.setResponseHeader(responseHeader);

            return clientHandler.wrap(JSONUtil.toJSON(response));


          //  System.out.println(request);


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
