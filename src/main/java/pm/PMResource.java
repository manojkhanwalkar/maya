package pm;


import com.codahale.metrics.annotation.Timed;
import data.PMRequest;
import data.PMResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Random;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class PMResource {
    private final String template;
    private final String defaultName;



    public PMResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;



    }



Random random = new Random() ;


    @POST
    @Timed
    @Path("/verify")
    @Produces(MediaType.APPLICATION_JSON)
    public PMResponse verify(PMRequest request) {
         PMResponse response =  new PMResponse();
         response.setScore(String.valueOf(random.nextInt(100)));

         return response;
    }







}
