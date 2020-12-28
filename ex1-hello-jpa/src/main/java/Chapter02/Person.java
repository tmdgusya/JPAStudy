package Chapter02;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Person {

    @Id @GeneratedValue
    @Column(name = "person_id")
    private Long id;

    @ManyToOne // N -> 1 관계이므로 ManyToOne 임 구성도 관계 그대로 생각하면됨.
    @JoinColumn(name = "team_id")
    private Team team;
    private String name;

    public void setTeam(Team team){
        // 기존에 연결되었던 관계를 정리해줘야함!
        if(this.team != null){
            this.team.getMembers().remove(this);
        }
        this.team = team;
        team.getMembers().add(this);
    }

}
