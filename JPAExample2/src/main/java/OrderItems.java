import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@Entity
public class OrderItems extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "orderitems_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne()
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;
}