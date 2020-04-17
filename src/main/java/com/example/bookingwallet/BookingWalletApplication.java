package com.example.bookingwallet;

import com.example.bookingwallet.health.TemplateHealthCheck;
import com.example.bookingwallet.resources.CampaignResource;
import com.example.bookingwallet.resources.WalletResource;
import com.example.bookingwallet.service.BookingWalletService;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jdbi3.JdbiHealthCheck;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.jdbi.v3.core.Jdbi;

public class BookingWalletApplication extends Application<BookingWalletConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BookingWalletApplication().run(args);
    }

    @Override
    public String getName() {
        return "booking-wallet";
    }

    @Override
    public void initialize(final Bootstrap<BookingWalletConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<BookingWalletConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(BookingWalletConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(final BookingWalletConfiguration configuration, final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");

        environment.healthChecks().register("database",
                new JdbiHealthCheck(jdbi, configuration.getDataSourceFactory().getValidationQuery()));

        //HealthCheck
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);


        //Resource
        BookingWalletService service = new BookingWalletService(jdbi);
        environment.jersey().register(new CampaignResource(service));
        environment.jersey().register(new WalletResource(service));
    }
}
