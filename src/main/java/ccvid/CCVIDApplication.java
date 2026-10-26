package ccvid;

import cm.CMResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class CCVIDApplication extends Application<CCVIDConfiguration> {
    public static void main(String[] args) throws Exception {
        new CCVIDApplication().run(args);
    }

    @Override
    public String getName() {
        return "Slacker Application";
    }

    @Override
    public void initialize(Bootstrap<CCVIDConfiguration> bootstrap) {
        // Resolve ${MAYA_KEYSTORE_PATH} / ${MAYA_KEYSTORE_PASSWORD} in the YAML from the environment
        // (strict: startup fails if a variable is missing, instead of running with a default secret).
        bootstrap.setConfigurationSourceProvider(new io.dropwizard.configuration.SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new io.dropwizard.configuration.EnvironmentVariableSubstitutor(true)));


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

        final CMResource resource1 = new CMResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );


        environment.jersey().register(resource);
        environment.jersey().register(resource1);

        //  environment.healthChecks().register("APIHealthCheck", new AppHealthCheck());




}

}