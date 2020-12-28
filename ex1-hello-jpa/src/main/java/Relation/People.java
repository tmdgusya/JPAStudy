package Relation;

import Chapter02.Team;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class People {

    @Id @GeneratedValue
    @Column(name = "people_id")
    private Long id;

    //Section 1,2
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    //Section 3
    // 해당 group Field 를 제거한다. 외래키를 Group에서 직접 관리한다.
    private String username;

    @OneToOne
    @JoinColumn(name = "locker_id")
    private Locker locker;
}
