package co.uk.fi.service;

import co.uk.fi.domain.Price;
import co.uk.fi.transformer.CSVPriceTransformer;
import co.uk.fi.transformer.PriceTransformer;
import co.uk.fi.util.TestDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

import static co.uk.fi.transformer.CSVPriceTransformer.formatter;
import static co.uk.fi.util.TestDataUtil.INVALID_PRICE_MULTI_RECORDS;
import static co.uk.fi.util.TestDataUtil.VALID_PRICE_MULTI_RECORDS;
import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
class FXPricingTickSubscriberTest {
    private FXPricingTickSubscriber subscriber;
    private PricingService pricingService;
    private PriceTransformer priceTransformer;
    private CountDownLatch latch;
    @BeforeEach
    void setUp() {
        priceTransformer = new CSVPriceTransformer();
        pricingService = new PricingServiceImpl(){
            @Override
            public void savePrice(Price price) {
                super.savePrice(price);
                latch.countDown();
            }
        };
        subscriber = new FXPricingTickSubscriber(pricingService, priceTransformer);
    }

    @Test
    void testOnMessageValidMessage() throws InterruptedException {
        latch = new CountDownLatch(1);
        subscriber.onMessage("110, EUR/JPY, 119.61,119.91,01-06-2020 12:01:02:110");
        latch.await();
        Price savedPrice = pricingService.getPrice("EUR/JPY");
        assertEquals("110", savedPrice.getTickId());
        assertEquals("EUR/JPY", savedPrice.getInstrumentName());
        assertEquals(valueOf(119.49), savedPrice.getBid());
        assertEquals(valueOf(120.03), savedPrice.getAsk());
        assertEquals("01-06-2020 12:01:02:110", formatter.format(savedPrice.getTimestamp()));
    }

    @Test
    void testOnMessageForPriceUpdate() throws InterruptedException {
        latch= new CountDownLatch(1);
        subscriber.onMessage(TestDataUtil.VALID_PRICE_SINGLE_RECORD);
        latch.await();
        Price savedPrice = pricingService.getPrice("EUR/JPY");
        assertEquals("110", savedPrice.getTickId());
        assertEquals("EUR/JPY", savedPrice.getInstrumentName());
        assertEquals(valueOf(119.29), savedPrice.getBid());
        assertEquals(valueOf(119.93), savedPrice.getAsk());
        assertEquals("01-06-2020 12:01:02:110", formatter.format(savedPrice.getTimestamp()));

        //Update same instrument with new price and validate price is updated for that instrument
        latch= new CountDownLatch(1);
        subscriber.onMessage("111, EUR/JPY, 119.61,119.91,01-06-2020 12:02:02:110");
        latch.await();
        //Validate all attributes against new price
        savedPrice = pricingService.getPrice("EUR/JPY");
        assertEquals("111", savedPrice.getTickId());
        assertEquals("EUR/JPY", savedPrice.getInstrumentName());
        assertEquals(valueOf(119.49), savedPrice.getBid());
        assertEquals(valueOf(120.03), savedPrice.getAsk());
        assertEquals("01-06-2020 12:02:02:110", formatter.format(savedPrice.getTimestamp()));
    }

    @Test
    void onMessageMultiplePrice() throws InterruptedException {
        latch = new CountDownLatch(7);
        subscriber.onMessage(VALID_PRICE_MULTI_RECORDS);
        latch.await();
        Price savedPrice = pricingService.getPrice("EUR/JPY");
        System.out.println("Price for EUR/JPY in cache : " + savedPrice);
        assertEquals("119", savedPrice.getTickId());
        assertEquals("EUR/JPY", savedPrice.getInstrumentName());
        assertEquals(valueOf(119.49), savedPrice.getBid());
        assertEquals(valueOf(120.03), savedPrice.getAsk());
        assertEquals("01-06-2020 12:01:10:110", formatter.format(savedPrice.getTimestamp()));
    }

    @Test
    void testOnMessageInValidMessageForSinglePrice() throws InterruptedException {
        subscriber.onMessage("110, EUR/JPY, ,119.91,01-06-2020 12:01:02:110");
        assertNull(pricingService.getPrice("EUR/JPY"), "Price should not be saved for EUR/JPY");
    }

    @Test
    void testOnMessageInValidMessageForMultiplePrice() throws InterruptedException {
        latch = new CountDownLatch(7);
        subscriber.onMessage(INVALID_PRICE_MULTI_RECORDS);
        latch.await();
        System.out.println("PRINTING: "+ pricingService.getAllPrices());
        Price savedPrice = pricingService.getPrice("EUR/JPY");
        System.out.println("Price for EUR/JPY in cache : " + savedPrice);
        assertEquals("121", savedPrice.getTickId());
        assertEquals("EUR/JPY", savedPrice.getInstrumentName());
        assertEquals(valueOf(119.49), savedPrice.getBid());
        assertEquals(valueOf(120.03), savedPrice.getAsk());
        assertEquals("01-06-2020 12:01:10:112", formatter.format(savedPrice.getTimestamp()));
    }
}