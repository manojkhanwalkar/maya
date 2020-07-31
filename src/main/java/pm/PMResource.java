package pm;


import com.codahale.metrics.annotation.Timed;
import data.PMRequest;
import data.PMResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
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

      // return Optional.empty();
    }





    @POST
    @Timed
    @Path("/verify1")
    @Produces(MediaType.APPLICATION_JSON)
 //   public Response verify1(PMRequest request) {
  public Optional<PMResponse> verify1(PMRequest request) {
        PMResponse response =  new PMResponse();
        response.setScore(String.valueOf(random.nextInt(100)));

        return Optional.of(response);

       // return Response.serverError().build();

       //return CCVIdResponse.status(CCVIdResponse.Status.BAD_GATEWAY).build();

     //  throw new WebApplicationException(404);
    }





}
