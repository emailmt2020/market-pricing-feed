package co.uk.fi.core;

import co.uk.fi.domain.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

@Slf4j
@Service
public class OrderBookImpl implements OrderBook {
    private final ConcurrentMap<String, PriorityQueue<BigDecimal>> bidMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, PriorityQueue<BigDecimal>> askMap = new ConcurrentHashMap<>();

    @Override
    public BigDecimal getBestBid(String instrument) {
        PriorityQueue<BigDecimal> prices = bidMap.get(instrument);
        BigDecimal bestBid = Objects.nonNull(prices) ? prices.peek() : null;
        log.info("Best Bid {} -> BIDMAP={} ", bestBid, bidMap);
        return bestBid;
    }

    @Override
    public BigDecimal getBestAsk(String instrument) {
        PriorityQueue<BigDecimal> prices = askMap.get(instrument);
        BigDecimal bestAsk = Objects.nonNull(prices) ? prices.peek() : null;
        log.info("Best Ask {} -> ASKMAP={} ", bestAsk, askMap);
        return bestAsk;
    }

    @Override
    public void updateBook(Price price) {
        updateBook(price.getBid(), price.getAsk(), price.getInstrumentName());
    }

    @Override
    public void updateBook(BigDecimal bid, BigDecimal ask, String instrument) {
        updateBidBook(instrument, bid);
        updateAskBook(instrument, ask);
    }

    private void updateBidBook(String instrument, BigDecimal bid) {
        bidMap.putIfAbsent(instrument, new PriorityQueue<>(Comparator.reverseOrder()));
        bidMap.computeIfPresent(instrument, (instrument1, bidPrices) -> {
            BigDecimal bestAsk = getBestAsk(instrument1);
            boolean isAcceptableBid = Objects.isNull(bestAsk) || bid.compareTo(bestAsk) > 0;
            if(isAcceptableBid ){
                bidPrices.add(bid);
            }else{
                log.info("Discarding bid {} -- ASKMAP={} ", bid.doubleValue(), askMap);
            }
            return bidPrices;
        });
    }

    private void updateAskBook(String instrument, BigDecimal ask) {
        askMap.putIfAbsent(instrument, new PriorityQueue<>(Comparator.naturalOrder()));
        askMap.computeIfPresent(instrument, (instrument1, askPrices) -> {
            BigDecimal bestBid = getBestBid(instrument1);
            if(Objects.isNull(bestBid) || ask.compareTo(bestBid) > 0){
                askPrices.add(ask);
            }else{
                log.info("Discarding ASK {} -- BIDMAP={} ", ask.doubleValue(), askMap);
            }
            return askPrices;
        });
    }
}
