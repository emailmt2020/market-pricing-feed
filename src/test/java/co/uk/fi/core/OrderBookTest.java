package co.uk.fi.core;

import co.uk.fi.domain.Price;
import co.uk.fi.transformer.CSVPriceTransformer;
import co.uk.fi.transformer.PriceTransformer;
import co.uk.fi.util.TestDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
@Slf4j
@RunWith(MockitoJUnitRunner.class)
class OrderBookTest {
    OrderBook orderBook;
    PriceTransformer transformer = new CSVPriceTransformer();
    @BeforeEach
    void setUp() {
        orderBook = new OrderBookImpl();
    }

    @Test
    void getBestBid() {
    }

    @Test
    void getBestAsk() {
    }

    @Test
    void testUpdateBookForSinglePrice() {
        Price price = TestDataUtil.createPrice();
        orderBook.updateBook(price.getBid(), price.getAsk(), price.getInstrumentName());
        assertEquals(price.getBid(), orderBook.getBestBid(price.getInstrumentName()));
        assertEquals(price.getAsk(), orderBook.getBestAsk(price.getInstrumentName()));
    }

    @Test
    void testUpdateBookForPriceUpdate() {
        Price price =  transformer.transform("110, EUR/JPY,119.51, 119.81, 01-06-2020 12:01:02:110");
        log.info("Adding Price: {} ", price);
        orderBook.updateBook(price);
        Price updatedPrice =  transformer.transform("110, EUR/JPY,119.91, 119.88,01-06-2020 12:01:02:110");
        log.info("Adding updatedPrice: {} ", updatedPrice);
        orderBook.updateBook(updatedPrice);
        assertEquals(BigDecimal.valueOf(119.91), orderBook.getBestBid(price.getInstrumentName()));
        assertEquals(BigDecimal.valueOf(119.81), orderBook.getBestAsk(price.getInstrumentName()));
    }

    @Test
    void testUpdateBookForMultiplePriceUpdates() {
        orderBook.updateBook(transformer.transform("110, EUR/JPY,119.41, 119.72, 01-06-2020 12:01:02:110"));
        orderBook.updateBook(transformer.transform("110, EUR/JPY,119.51, 119.82, 01-06-2020 12:01:02:111"));
        orderBook.updateBook(transformer.transform("110, EUR/JPY,119.61, 119.92, 01-06-2020 12:01:02:112"));
        orderBook.updateBook(transformer.transform("110, EUR/JPY,119.71, 119.99, 01-06-2020 12:01:02:113"));
        orderBook.updateBook(transformer.transform("110, EUR/JPY,119.81, 119.89, 01-06-2020 12:01:02:114"));
        orderBook.updateBook(transformer.transform("110, EUR/JPY,119.91, 119.85, 01-06-2020 12:01:02:115"));
        assertEquals(BigDecimal.valueOf(119.72), orderBook.getBestAsk("EUR/JPY"));
        assertEquals(BigDecimal.valueOf(119.91), orderBook.getBestBid("EUR/JPY"));
    }
}