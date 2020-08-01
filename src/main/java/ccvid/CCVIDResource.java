package ccvid;


import com.codahale.metrics.annotation.Timed;
import com.nimbusds.jose.jwk.JWKSet;
import common.Processor;
import data.*;
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
public class CCVIDResource {
    private final String template;
    private final String defaultName;



    public CCVIDResource(String template, String defaultName) {
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


    Processor<String,String> processor = new CCVIDRequestProcessor();

    @POST
    @Timed
    @Path("/verify")
    @Produces(MediaType.APPLICATION_JSON)
    public EncryptedSignedResponse verify(EncryptedSignedRequest encryptedSignedRequest) {


        return ClientManager.process(encryptedSignedRequest,clientCache,processor);


    }







}
