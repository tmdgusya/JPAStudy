package Relation3;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED) // 상속관계 맵핑 전
@DiscriminatorColumn(name = "DTYPE") // 지정된 자식 테이블을 구분 하기 위함 Column 을 하나 생성하는 거임
public abstract class Items {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
}
