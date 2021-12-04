package co.uk.fi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Builder
@Getter
public class Price {
    private final String tickId;
    private final String instrumentName;
    private final BigDecimal bid;
    private final BigDecimal ask;
    private final LocalDateTime timestamp;

    @Override
    public String toString() {
        return "Price{" +
                "instrumentId='" + tickId + '\'' +
                ", instrumentName='" + instrumentName + '\'' +
                ", bid=" + bid +
                ", ask=" + ask +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
