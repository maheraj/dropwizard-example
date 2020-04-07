package com.example.bookingwallet;

import com.example.bookingwallet.health.TemplateHealthCheck;
import com.example.bookingwallet.resources.HelloWorldResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        // TODO: application initialization
    }

    @Override
    public void run(final BookingWalletConfiguration configuration,
                    final Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }

}
