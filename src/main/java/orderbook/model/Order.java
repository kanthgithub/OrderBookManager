package orderbook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private Long tradeId;
    private String ticker;
    private Long sequence;
    private Double price;
    private String side;
    private Double lastSize;
    private Double openPrice;
    private Double bestBid;
    private Double bestAsk;
    private Long time;

    @Override
    public String toString() {
        return ("\n| Order\ttime= " + time +
                "\ttradeId = " + tradeId +
                "\tticker = " + ticker +
                "\tside = " + side +
                "\tprice = " + price +
                "\tquantity = " + lastSize );
    }
}
