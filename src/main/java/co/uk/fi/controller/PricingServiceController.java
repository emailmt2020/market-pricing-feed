package co.uk.fi.controller;

import co.uk.fi.service.PricingService;
import co.uk.fi.domain.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@Slf4j
public class PricingServiceController {
    @Autowired
    PricingService pricingService;

    @GetMapping("/")
    public String index() {
        return "Pricing Service Up and Running!";
    }

    @GetMapping("/getPrice")
    public ResponseEntity<String> getPrice(@RequestParam String instrumentId) {
        log.info("Received new price request for: " + instrumentId);
        Price price = pricingService.getPrice(instrumentId);
        if (Objects.nonNull(price)) {
            return new ResponseEntity<String>(price.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Price not found for requested Instrument - " + instrumentId, HttpStatus.NOT_FOUND);
        }
    }

}
