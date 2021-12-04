package co.uk.fi.service;

import co.uk.fi.domain.Price;
import co.uk.fi.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.Math.random;
import static java.lang.String.valueOf;
import static org.junit.Assert.*;

class PricingServiceTest {
    PricingService pricingService;

    @BeforeEach
    void setUp() {
        pricingService = new PricingServiceImpl();
    }

    @Test
    void testUpdatePriceForNewInstrument() {
        Price price = TestDataUtil.createPrice();
        pricingService.savePrice(price);
        Price savedPrice = pricingService.getPrice(price.getInstrumentName());
        validatePrice(price, savedPrice);
    }

    @Test
    void getPriceForNullId() {
        Price price = pricingService.getPrice(null);
        assertNull(price);
    }

    @Test
    void getPriceForUnavailableInstrumentId() {
        Price price = pricingService.getPrice(valueOf(random()));
        assertNull(price);
    }

    @Test
    void testGetPriceForNull() {
        assertNull(pricingService.getPrice(null));
    }

    @Test
    void testgetPriceForLatestInstrumentPrice() {
        Price price = TestDataUtil.createPrice();
        pricingService.savePrice(price);
        assertEquals(price.getTickId(), pricingService.getPrice(price.getInstrumentName()).getTickId());
        //Now Update same instrument with some different price
        Price newPrice = TestDataUtil.createPrice(1);
        pricingService.savePrice(newPrice);
        //Now latest price should give you last published price
        Price latestPrice = pricingService.getPrice(price.getInstrumentName());
        validatePrice(newPrice, latestPrice);
    }

    private void validatePrice(Price expectedPrice, Price actualPrice) {
        assertEquals(expectedPrice.getTickId(), actualPrice.getTickId());
        assertEquals(expectedPrice.getInstrumentName(), actualPrice.getInstrumentName());
        assertEquals(expectedPrice.getAsk(), actualPrice.getAsk());
        assertEquals(expectedPrice.getBid(), actualPrice.getBid());
        assertEquals(expectedPrice.getTimestamp(), actualPrice.getTimestamp());
    }


}