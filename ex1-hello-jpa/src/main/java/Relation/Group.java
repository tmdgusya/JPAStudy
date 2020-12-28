package Relation;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Group {

    @Id @GeneratedValue
    @Column(name = "group_id")
    private Long id;

    private String name;

    //Section 1. 다대일 단방향 관
    //여기는 List<People> 을 쓰지않음. 다대일 단방향 관계에서는 One 쪽에서는 참조가 불가능함! 단방향이므로!

    //Section 2. 다대일 양방향 관계 서로 참조가 가능하다!
    @OneToMany(mappedBy = "group")
    private List<People> people = new ArrayList<>();
    //Section 3. 일대다 단방향 관계
    /*
    * OneToMany()
    * JoinColumn(name = "group_id") // -> 이게 Member Table 의 team_id (FK) 를 지칭한다.
    *
    * */
}
