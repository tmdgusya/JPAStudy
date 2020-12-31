import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    private int orderAmount;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "order")
    private Product product;
}
