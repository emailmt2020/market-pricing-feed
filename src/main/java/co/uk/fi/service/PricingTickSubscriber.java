package co.uk.fi.service;

public interface PricingTickSubscriber {
    void onMessage(String csvPrices);
}
