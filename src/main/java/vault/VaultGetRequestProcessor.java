package vault;

import common.Processor;
import data.VaultGetResponse;
import data.VaultPutRequest;
import data.VaultPutResponse;
import util.JSONUtil;

public class VaultGetRequestProcessor implements Processor<String,String> {


    @Override
    public String process(String payloadUnwrapped) {


        VaultGetResponse response = new VaultGetResponse();

        try {

            VaultGetResponse request  = (VaultGetResponse) JSONUtil.fromJSON(payloadUnwrapped, VaultGetResponse.class);




            return JSONUtil.toJSON(response);




        } catch (Exception e) {
            e.printStackTrace();
        }



        return null ;



    }



}
