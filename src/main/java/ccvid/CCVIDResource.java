package ccvid;


import com.codahale.metrics.annotation.Timed;
import data.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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



Random random = new Random() ;


    @POST
    @Timed
    @Path("/keyexchange")
    @Produces(MediaType.APPLICATION_JSON)
    public KeyExchange keyexchange(KeyExchange request) {


        ClientHandler clientHandler = new ClientHandler();
        clientHandler.init(request.getJavawebKeySet());


        KeyExchange response = new KeyExchange();
        response.setJavawebKeySet(clientHandler.getKAandSignPubKeys());

        return response;
    }



  /*  @POST
    @Timed
    @Path("/verify")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verify(EncryptedSignedRequest request) {

        return null;
    }*/







}
