package co.uk.fi.business;

import co.uk.fi.domain.Price;
import co.uk.fi.util.TestDataUtil;
import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.*;

class MarginCalculatorTest {

    @Test
    void applyMargin() {
        Price inPrice= TestDataUtil.createPrice();
        Price outPrice = MarginCalculator.applyMargin(inPrice);
        assertEquals(outPrice.getTickId(),inPrice.getTickId());
        assertEquals(valueOf(112.11),outPrice.getAsk());
        assertEquals(valueOf(110.89),outPrice.getBid());
        assertEquals(outPrice.getTimestamp(),inPrice.getTimestamp());
    }

    @Test
    void applyCommissionOnAskPrice() {
        assertEquals(valueOf(112.11),MarginCalculator.applyCommissionOnAskPrice(valueOf(112)));
    }

    @Test
    void applyCommissionOnBidPrice() {
        assertEquals(valueOf(110.89),MarginCalculator.applyCommissionOnBidPrice(valueOf(111)));
    }
}