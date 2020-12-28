package Chapter01.order;

import Chapter01.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ORDERS")
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "member_id")
    private Member member;

    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    //OrderItem 을 List 로 뽑아와서 가격을 전부 더한다.
    //Price 는 물품을 다사면 더해지는 가격이다. 따라서 일단 default 는 0으로 한다.
    @Column(columnDefinition = "integer default 0")
    private int price;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    public void setMember(Member member){
        if(this.member != null){
            this.member.getOrders().remove(this);
        }
        this.member = member;
        member.getOrders().add(this);
    }

    public void setOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
