package Relation04;

import javax.persistence.*;

@Entity
public class Child {

    @Id
    private String id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "parent_id1", referencedColumnName = "parent_id1"),
            @JoinColumn(name = "parent_id2", referencedColumnName = "parent_id2")
    })
    private Parent parent;
}
