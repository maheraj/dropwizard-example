package com.example.bookingwallet;

import com.example.bookingwallet.resources.CampaignResource;
import com.example.bookingwallet.resources.CustomerResource;
import com.example.bookingwallet.resources.TransactionResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

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
    public void run(final BookingWalletConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(new CampaignResource());
        environment.jersey().register(new CustomerResource());
        environment.jersey().register(new TransactionResource());
    }
}
