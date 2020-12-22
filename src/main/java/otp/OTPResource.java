package otp;


import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Random;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class OTPResource {
    private final String template;
    private final String defaultName;



    public OTPResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;



    }



Random random = new Random() ;


    @POST
    @Timed
    @Path("/verify")
    @Produces(MediaType.APPLICATION_JSON)
    public OTPValidateResponse verify(OTPValidate request) {

        return null;

    }





    @POST
    @Timed
    @Path("/otp")
    @Produces(MediaType.APPLICATION_JSON)

  public OTPResponse otp(OTPRequest request) {

        return null;
    }





}
