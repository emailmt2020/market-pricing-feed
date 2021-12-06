package co.uk.fi.transformer;

import co.uk.fi.domain.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class CSVPriceTransformer implements PriceTransformer {
    public final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");
    @Override
    public Price transform(String priceMessage) {
        String[] cols = priceMessage.split(",");
        if(cols.length==5){
            return Price.builder().tickId(cols[0].trim())
                    .instrumentName(cols[1].trim())
                    .bid(new BigDecimal(cols[2].trim()))
                    .ask(new BigDecimal(cols[3].trim()))
                    .timestamp(LocalDateTime.parse(cols[4].trim(), formatter)).build();

        }else{
            throw new RuntimeException("Unexpected number of attributes in Price message: " + priceMessage);
        }
    }
}
