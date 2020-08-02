package vault;

import common.Processor;
import data.VaultGetRequest;
import data.VaultGetResponse;
import data.VaultPutRequest;
import data.VaultPutResponse;
import util.JSONUtil;

import java.util.Optional;
import java.util.UUID;

public class VaultGetRequestProcessor implements Processor<String,String> {

TokenManager tokenManager ;

    public VaultGetRequestProcessor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public String process(String payloadUnwrapped) {


        VaultGetResponse response = new VaultGetResponse();

        try {

            VaultGetRequest request  = (VaultGetRequest) JSONUtil.fromJSON(payloadUnwrapped, VaultGetRequest.class);

            Optional<String> pan = tokenManager.get(request.getMerchantId(),request.getToken());


            response.setCcNumber(pan.get());

            response.setRequestId(request.getRequestId());

            response.setResponseId(UUID.randomUUID().toString());

            return JSONUtil.toJSON(response);




        } catch (Exception e) {
            e.printStackTrace();
        }



        return null ;



    }



}
