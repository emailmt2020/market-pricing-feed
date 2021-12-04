package co.uk.fi.service;

import co.uk.fi.domain.Price;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface PricingService {
    void savePrice(Price price);
    Price getPrice(String instrumentName);
    void clearPrices();

    Map<String, Price> getAllPrices();
}
