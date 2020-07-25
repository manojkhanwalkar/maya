package pm;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class PMApplication extends Application<PMConfiguration> {
    public static void main(String[] args) throws Exception {
        new PMApplication().run(args);
    }

    @Override
    public String getName() {
        return "Slacker Application";
    }

    @Override
    public void initialize(Bootstrap<PMConfiguration> bootstrap) {

    //    IDStatusPollManager.getInstance().start();

        // nothing to do yet
    }

    @Override
    public void run(PMConfiguration configuration,
                    Environment environment) {
        final PMResource resource = new PMResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );


        environment.jersey().register(resource);
      //  environment.healthChecks().register("APIHealthCheck", new AppHealthCheck());




}

}