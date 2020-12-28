package Chapter01.member;

import Chapter01.order.Order;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Getter
public class Member {

    public Member(){}
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 10)
    private String name;
    private String city;
    private String street;
    private String zipcode;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
