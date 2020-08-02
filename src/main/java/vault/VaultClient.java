package vault;


import data.*;
import util.CardUtil;
import util.JSONUtil;
import util.Sender;

import java.util.UUID;

public class VaultClient {


    public static void main(String[] args) throws Exception  {


        Sender sender = new Sender("https://localhost:9180/");

        sender.init();

        Token.Type[] types = Token.Type.values();

        String cardNum = CardUtil.createVCard();

        for (int i=0;i<types.length;i++) {
            Token token = null;

            {
                VaultPutRequest putRequest = new VaultPutRequest();

                putRequest.setCcNumber(cardNum);
                putRequest.setMerchantId("Merchant1");
                putRequest.setRequestId(UUID.randomUUID().toString());

                putRequest.setType(types[i]);

                String payload = JSONUtil.toJSON(putRequest);


                String responseStr = sender.send(payload, "tokenize");

                VaultPutResponse vaultPutResponse = (VaultPutResponse) JSONUtil.fromJSON(responseStr, VaultPutResponse.class);

                token = vaultPutResponse.getToken();

                System.out.println(vaultPutResponse);

            }

            {
                VaultGetRequest getRequest = new VaultGetRequest();

                getRequest.setMerchantId("Merchant1");
                getRequest.setRequestId(UUID.randomUUID().toString());
                getRequest.setToken(token);


                String payload = JSONUtil.toJSON(getRequest);


                String responseStr = sender.send(payload, "cc");

                VaultGetResponse vaultGetResponse = (VaultGetResponse) JSONUtil.fromJSON(responseStr, VaultGetResponse.class);

                System.out.println(vaultGetResponse);

            }

            //System.out.println(response);

        }

    }



}
