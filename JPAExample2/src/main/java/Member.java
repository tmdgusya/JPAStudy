import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
public class Member extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;
    @Embedded
    private Address homeaddress;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="city", column = @Column(name = "work_city")),
        @AttributeOverride(name="street", column = @Column(name = "work_street")),
        @AttributeOverride(name="zipcode", column = @Column(name = "work_zipcode"))
    })
    private Address workaddress;

    // Collection 은 지연로딩을 기본으로 지원
    @ElementCollection
    @CollectionTable(name = "ADDRESS_LIST", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    private List<Address> addressesList = new ArrayList<>();


    @Embedded
    private Period workPeriod;

    @OneToMany(mappedBy = "member")
    private List<Order> orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

}
