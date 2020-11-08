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
public class CoinBaseOrderEvent {

    private String type;
    private Long sequence;
    private String product_id;
    private String open_24h;
    private String volume_24h;
    private String low_24h;
    private String high_24h;
    private String volume_30d;
    private String best_bid;
    private String best_ask;
    private String price;
    private String side;
    private String time;
    private Long trade_id;
    private String last_size;

}
