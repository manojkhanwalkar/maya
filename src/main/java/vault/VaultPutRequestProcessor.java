package vault;

import common.Processor;
import data.*;
import pm.PMClient;
import util.ClientManager;
import util.JSONUtil;
import vfs.TransactionData;
import vfs.VFSClient;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class VaultPutRequestProcessor implements Processor<String,String> {


    TokenManager tokenManager;

    public VaultPutRequestProcessor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public String process(String payloadUnwrapped) {


        VaultPutResponse response = new VaultPutResponse();

        try {

            VaultPutRequest request  = (VaultPutRequest) JSONUtil.fromJSON(payloadUnwrapped, VaultPutRequest.class);

            Optional<Token> token = tokenManager.put(request.getMerchantId(),request.getCcNumber(),request.getType());


            response.setRequestId(request.getRequestId());
            response.setResponseId(UUID.randomUUID().toString());
            response.setToken(token.get());
            return JSONUtil.toJSON(response);




        } catch (Exception e) {
            e.printStackTrace();
        }



        return null ;



    }



}
