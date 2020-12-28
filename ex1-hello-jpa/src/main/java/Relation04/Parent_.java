package Relation04;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Parent_ {

    @EmbeddedId
    private ParentID_ id;

    private String name;
}
