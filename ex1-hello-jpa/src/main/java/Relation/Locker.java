package Relation;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Locker {

    @Id @GeneratedValue
    @Column(name = "locker_id")
    private Long id;
    private String name;
    @OneToOne(mappedBy = "locker") // 주 테이블에 외래키를 관리하도록 해
    private People people;
}
