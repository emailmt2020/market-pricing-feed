package co.uk.fi.transformer;

import co.uk.fi.domain.Price;

@FunctionalInterface
public interface PriceTransformer {
    Price transform(String priceMessage);
}
