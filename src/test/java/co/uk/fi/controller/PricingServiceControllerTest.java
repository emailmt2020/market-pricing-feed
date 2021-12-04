package co.uk.fi.controller;

import co.uk.fi.domain.Price;
import co.uk.fi.service.FXPricingTickSubscriber;
import co.uk.fi.service.PricingService;
import co.uk.fi.service.PricingServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PricingServiceControllerTest {
    @Autowired
    private FXPricingTickSubscriber subscriber;
    @Autowired
    private MockMvc mvc;

    @Test
    public void testPriceRunning() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Pricing Service Up and Running!")));
    }

    @Test
    void testGetPriceForUnknownId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/getPrice")
                .param("instrumentId", "unknownId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(equalTo("Price not found for requested Instrument - unknownId")));
    }

    @Test
    void testGetPriceAfterAddingInstrument() throws Exception {
        subscriber.onMessage("110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110");
        //Temp solution to make price available as this is not in scope of given requirement.
        Thread.sleep(1000);
        mvc.perform(MockMvcRequestBuilders.get("/getPrice")
                .param("instrumentId", "EUR/JPY")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Price{instrumentId='110', instrumentName='EUR/JPY', bid=119.49, ask=120.03, timestamp='2020-06-01T12:01:02.110'}")));
    }
}
