package vault;

import ccvid.CCVIDConfiguration;
import ccvid.CCVIDResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class VaultApplication extends Application<VaultConfiguration> {
    public static void main(String[] args) throws Exception {
        new VaultApplication().run(args);
    }

    @Override
    public String getName() {
        return "Slacker Application";
    }

    @Override
    public void initialize(Bootstrap<VaultConfiguration> bootstrap) {
        // Resolve ${MAYA_KEYSTORE_PATH} / ${MAYA_KEYSTORE_PASSWORD} in the YAML from the environment
        // (strict: startup fails if a variable is missing, instead of running with a default secret).
        bootstrap.setConfigurationSourceProvider(new io.dropwizard.configuration.SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(),
                new io.dropwizard.configuration.EnvironmentVariableSubstitutor(true)));


    //    IDStatusPollManager.getInstance().start();

        // nothing to do yet
    }

    @Override
    public void run(VaultConfiguration configuration,
                    Environment environment) {
        final VaultResource resource = new VaultResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );


        environment.jersey().register(resource);
      //  environment.healthChecks().register("APIHealthCheck", new AppHealthCheck());




}

}