package Chapter01.order;

import Chapter01.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    //Join 시킬것! 밑의 두 Property는!
    private int orderPrice;

    private int count;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
