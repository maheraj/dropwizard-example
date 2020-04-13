package com.example.bookingwallet;

import com.example.bookingwallet.api.request.CreateCampaignRequest;
import com.example.bookingwallet.api.response.CampaignResponse;
import com.example.bookingwallet.core.Campaign;
import com.example.bookingwallet.db.CampaignDB;
import com.example.bookingwallet.resources.CampaignResource;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

//@ExtendWith(DropwizardExtensionsSupport.class)
public class CampaignResourceTesting {
//    private static final CampaignDB DAO = mock(CampaignDB.class);
//
//    public static final ResourceExtension RULE = ResourceExtension.builder()
//            .addResource(new CampaignResource(DAO))
//            //add all necessary resource
//            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
//            .build();
//
//    private Campaign campaign;
//
//    @BeforeEach
//    public void setup() {
//        campaign = new Campaign();
//        campaign.setId(1L);
//    }
//
//    @AfterEach
//    public void tearDown() {
//        reset(DAO);
//    }
//
//    @Test
//    public void getCampaignTest() {
//        CampaignResponse found = RULE.target("/campaigns/1").request().get(CampaignResponse.class);
//        assertThat(found.getId()).isEqualTo(campaign.getId());
//    }
//
//    @Test
//    public void createCampaignTest() {
//        // first call - create a campaign:: campaign obj
//        // 2nd call - create a customer wallet: wallet obj
//        // 3rd call - issue credit from campaign to customer: transaction obj
//        // 4th call - redeem credit - wall obj : transaction obj
//        // refund customer :transaction obj
//        // refund funding wallet:  transaction obj
//
//
//        // one method to implement the flow
//        CreateCampaignRequest request = new CreateCampaignRequest();
//        request.setName("Funding Campaign 2");
//        request.setCurrency("USD");
//        request.setBudget(100);
//        CampaignResponse newCampaign = RULE.target("/campaigns").request().
//                post(Entity.entity(request, MediaType.APPLICATION_JSON), CampaignResponse.class);
//        assertThat(newCampaign).isNotNull();
//    }


}
