package co.uk.fi.business;

import co.uk.fi.domain.Price;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_DOWN;
import static java.math.BigDecimal.valueOf;

public interface MarginCalculator {

    BigDecimal COMMISSION = valueOf(0.1);
    int DEFAULT_PRICING_SCALE = 2;

    static Price applyMargin(Price p) {
        return Price.builder().tickId(p.getTickId())
                .instrumentName(p.getInstrumentName())
                .ask(applyCommissionOnAskPrice(p.getAsk()))
                .bid(applyCommissionOnBidPrice(p.getBid()))
                .timestamp(p.getTimestamp())
                .build();
    }

    static BigDecimal applyCommissionOnAskPrice(BigDecimal askPrice) {
        return askPrice.add(askPrice.multiply(COMMISSION).divide(valueOf(100)).setScale(DEFAULT_PRICING_SCALE, ROUND_HALF_DOWN));
    }

    static BigDecimal applyCommissionOnBidPrice(BigDecimal bidPrice) {
        return bidPrice.subtract(bidPrice.multiply(COMMISSION).divide(valueOf(100)).setScale(DEFAULT_PRICING_SCALE, ROUND_HALF_DOWN));
    }
}
