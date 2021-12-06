package co.uk.fi.core;

import co.uk.fi.domain.Price;

import java.math.BigDecimal;

public interface OrderBook {
    BigDecimal getBestBid(String instrument);
    BigDecimal getBestAsk(String instrument);
    void updateBook(BigDecimal bid, BigDecimal ask, String instrument);
    void updateBook(Price price);
}
