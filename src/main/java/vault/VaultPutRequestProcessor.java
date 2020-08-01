package vault;

import common.Processor;
import data.*;
import pm.PMClient;
import util.ClientManager;
import util.JSONUtil;
import vfs.TransactionData;
import vfs.VFSClient;

import java.util.Date;
import java.util.UUID;

public class VaultPutRequestProcessor implements Processor<String,String> {


    @Override
    public String process(String payloadUnwrapped) {


        VaultPutResponse response = new VaultPutResponse();

        try {

            VaultPutRequest request  = (VaultPutRequest) JSONUtil.fromJSON(payloadUnwrapped, VaultPutRequest.class);




            return JSONUtil.toJSON(response);




        } catch (Exception e) {
            e.printStackTrace();
        }



        return null ;



    }



}
