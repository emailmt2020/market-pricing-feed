package co.uk.fi.service;

import co.uk.fi.domain.Price;
import co.uk.fi.transformer.PriceTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Scanner;

import static co.uk.fi.business.MarginCalculator.applyMargin;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@Slf4j
@Service
@RequiredArgsConstructor
public class FXPricingTickSubscriber implements PricingTickSubscriber {
    @Autowired
    final PricingService pricingService;
    @Autowired
    final PriceTransformer priceTransformer;

    @Override
    public void onMessage(String csvPrices) {
        log.info("Received Price Message Received: {} ", csvPrices);
        Scanner scanner = new Scanner(csvPrices);
        while (scanner.hasNextLine()) {
            String priceLine = scanner.nextLine();
            supplyAsync(() -> priceTransformer.transform(priceLine))
                    .thenApply(p -> applyMargin(p))
                    .thenApply(p -> storePrice(p))
                    .thenAccept(p -> publishPrice(p))
                    .exceptionally(ex -> {
                        log.error("Recovered from Exception: ", ex);
                        return null;
                    });
        }
        log.info("Price Message processed successfully");
    }

    private Price storePrice(Price p) {
        pricingService.savePrice(p);
        log.info("Price Saved successfully for instrument ID {}", p.getTickId());
        return p;
    }

    private void publishPrice(Price p) {
        log.info("Publish New Available Price to REST END POINT. Impl not in scope.");
    }
}
