import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Product {

    @Id @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;
    private String name;
    private int price;
    private int stockAmount;

    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;
}
