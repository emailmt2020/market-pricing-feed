package co.uk.fi.service;

import co.uk.fi.domain.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class PricingServiceImpl implements PricingService {
    ConcurrentMap<String, Price> priceMap = new ConcurrentHashMap<>();

    public void savePrice(Price price) {
        priceMap.compute(price.getInstrumentName(), new BiFunction<String, Price, Price>() {
            @Override
            public Price apply(String s, Price oldPrice) {
                if(Objects.isNull(oldPrice) || oldPrice.getTimestamp().compareTo(price.getTimestamp()) < 0){
                    log.info("New Price Added for instrument {} with id {}", price.getInstrumentName(), price.getTickId());
                    return price;
                } else{
                    log.info("New Price Discarded for instrument {} with id {}", price.getInstrumentName(), price.getTickId());
                    return oldPrice;
                }
            }
        });
    }

    public Price getPrice(String instrumentName) {
        return isNull(instrumentName)? null : priceMap.get(instrumentName);
    }

    public void clearPrices() {
        priceMap.clear();
    }

    @Override
    public Map<String, Price> getAllPrices() {
        return priceMap;
    }
}

