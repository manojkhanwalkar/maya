package ccvid;

import com.nimbusds.jose.JWEObject;
import data.*;
import util.JSONUtil;

import java.util.UUID;

public class ClientManager {

    public enum status { Success, Error}

    public static EncryptedSignedResponse process(EncryptedSignedRequest encryptedSignedRequest, ClientCache clientCache)
    {
        Response response = new Response();
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setResponseId(UUID.randomUUID().toString());

        try {
            String payload = encryptedSignedRequest.getEncryptedSignedPayload();

            var jweObject = JWEObject.parse(payload);

            String keyId = jweObject.getHeader().getKeyID();

            ClientHandler clientHandler = clientCache.get(keyId).orElseThrow();

            Request request = clientHandler.unwrap(payload);

            responseHeader.setRequestId(request.getRequestHeader().getRequestId());

            responseHeader.setStatus(status.Success.toString());

            responseHeader.setMessage("Processed request successfully");

            //TODO - send to PM and VFS and get a response from them .

            response.setResponseHeader(responseHeader);

            return clientHandler.wrap(JSONUtil.toJSON(response));


          //  System.out.println(request);


        } catch (Exception e) {
            e.printStackTrace();
        }



        return null ;
    }
}
