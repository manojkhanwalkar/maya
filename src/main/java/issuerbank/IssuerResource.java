package issuerbank;


import com.codahale.metrics.annotation.Timed;
import common.Processor;
import data.EncryptedSignedRequest;
import data.EncryptedSignedResponse;
import data.KeyExchange;
import util.ClientCache;
import util.ClientManager;
import util.KeyExchangeUtil;
import vault.TokenManager;
import vault.VaultGetRequestProcessor;
import vault.VaultPutRequestProcessor;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class IssuerResource {
    private final String template;
    private final String defaultName;



    public IssuerResource(String template, String defaultName) {
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



    CardIssuer cardIssuer = new CardIssuer();

    @POST
    @Timed
    @Path("/issue")
    @Produces(MediaType.APPLICATION_JSON)
    public EncryptedSignedResponse issue(EncryptedSignedRequest encryptedSignedRequest) {


       return ClientManager.process(encryptedSignedRequest,clientCache,cardIssuer);


    }


    @POST
    @Timed
    @Path("/transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public EncryptedSignedResponse transactions(EncryptedSignedRequest encryptedSignedRequest) {


      //  return ClientManager.process(encryptedSignedRequest,clientCache,getRequestProcessor);

        return null;

    }




}
