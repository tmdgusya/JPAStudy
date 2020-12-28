package Relation04;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Setter
@Getter
@IdClass(ParentID.class)
public class Parent {

    @Id
    @Column(name = "parent_id1")
    private String id1;

    @Id
    @Column(name = "parent_id2")
    private String id2;

    private String name;
}
