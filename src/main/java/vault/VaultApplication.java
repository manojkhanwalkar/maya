package vault;

import ccvid.CCVIDConfiguration;
import ccvid.CCVIDResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class VaultApplication extends Application<CCVIDConfiguration> {
    public static void main(String[] args) throws Exception {
        new VaultApplication().run(args);
    }

    @Override
    public String getName() {
        return "Slacker Application";
    }

    @Override
    public void initialize(Bootstrap<CCVIDConfiguration> bootstrap) {

    //    IDStatusPollManager.getInstance().start();

        // nothing to do yet
    }

    @Override
    public void run(CCVIDConfiguration configuration,
                    Environment environment) {
        final CCVIDResource resource = new CCVIDResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );


        environment.jersey().register(resource);
      //  environment.healthChecks().register("APIHealthCheck", new AppHealthCheck());




}

}