package cm;


import com.codahale.metrics.annotation.Timed;
import common.Processor;
import data.*;
import util.ClientCache;
import util.ClientManager;
import util.KeyExchangeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class CMResource {
    private final String template;
    private final String defaultName;



    public CMResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;



    }




    @POST
    @Timed
    @Path("/click")
    @Produces(MediaType.APPLICATION_JSON)
    public ClickResponse click( ClickRequest request) {

        System.out.println("Called click" );

        URI uri2 = UriBuilder.fromUri("https://localhost:8180/offer").build();
        Response response = Response.seeOther(uri2).header("ClickResponse" , "Some Base64 encoded string").build();

        throw new WebApplicationException(response);

    }



    @POST
    @Timed
    @Path("/offer")
    @Produces(MediaType.APPLICATION_JSON)
    public OfferResponse offer(OfferRequest orequest , @Context HttpServletRequest request) {


        System.out.println("Called offer for " + request.getHeader("ClickResponse") );
        return new OfferResponse();

    }









}
