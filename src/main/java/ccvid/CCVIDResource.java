package ccvid;


import com.codahale.metrics.annotation.Timed;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import data.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.util.Random;


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


        try {
            ClientHandler clientHandler = new ClientHandler();
            clientHandler.init(request.getJavawebKeySet());


            KeyExchange response = new KeyExchange();
            response.setJavawebKeySet(clientHandler.getKAandSignPubKeys());

            clientCache.put(JWKSet.parse(request.getJavawebKeySet()),clientHandler);
            clientCache.put(JWKSet.parse(response.getJavawebKeySet()),clientHandler);

            return response;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }



    @POST
    @Timed
    @Path("/verify")
    @Produces(MediaType.APPLICATION_JSON)
    public EncryptedSignedResponse verify(EncryptedSignedRequest encryptedSignedRequest) {


        return ClientManager.process(encryptedSignedRequest,clientCache);


    }







}
