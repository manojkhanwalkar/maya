package vault;


import ccvid.CCVIDRequestProcessor;
import com.codahale.metrics.annotation.Timed;
import com.nimbusds.jose.jwk.JWKSet;
import common.Processor;
import data.EncryptedSignedRequest;
import data.EncryptedSignedResponse;
import data.KeyExchange;
import util.ClientCache;
import util.ClientHandler;
import util.ClientManager;
import util.KeyExchangeUtil;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class VaultResource {
    private final String template;
    private final String defaultName;



    public VaultResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;



    }



ClientCache clientCache = new ClientCache();


    @POST
    @Timed
    @Path("/keyexchange")
    @Produces(MediaType.APPLICATION_JSON)
    public KeyExchange keyexchange(KeyExchange request) {

        return KeyExchangeUtil.keyExchange(request,clientCache);
    }


    Processor<String,String> putRequestProcessor = new VaultPutRequestProcessor();
    Processor<String,String> getRequestProcessor = new VaultGetRequestProcessor();

    @POST
    @Timed
    @Path("/tokenize")
    @Produces(MediaType.APPLICATION_JSON)
    public EncryptedSignedResponse tokenize(EncryptedSignedRequest encryptedSignedRequest) {


        System.out.println("Received request");
        return ClientManager.process(encryptedSignedRequest,clientCache,putRequestProcessor);


    }


    @POST
    @Timed
    @Path("/cc")
    @Produces(MediaType.APPLICATION_JSON)
    public EncryptedSignedResponse cc(EncryptedSignedRequest encryptedSignedRequest) {


        return ClientManager.process(encryptedSignedRequest,clientCache,getRequestProcessor);


    }




}
