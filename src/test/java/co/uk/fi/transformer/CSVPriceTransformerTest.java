package co.uk.fi.transformer;

import co.uk.fi.domain.Price;
import co.uk.fi.util.TestDataUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CSVPriceTransformerTest {
    private PriceTransformer transformer = new CSVPriceTransformer();

    @Test
    void testTransformValidPriceRecord() {
        Price price = transformer.transform(TestDataUtil.VALID_PRICE_SINGLE_RECORD);
        assertNotNull(price);
    }

    @Test
    void tesTransformPriceWithInvalidAttributeCount() {
        assertThrows(RuntimeException.class, () -> transformer.transform("110, EUR/JPY,01-06-2020 12:01:02:110"));
    }

    @Test
    void tesTransformPriceWithInvalidFormat() {
        assertThrows(RuntimeException.class, () -> transformer.transform("110, EUR/JPY,,119.81,01-06-2020 12:01:02:110"));
    }
}