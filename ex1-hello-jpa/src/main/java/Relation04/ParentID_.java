package Relation04;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class ParentID_ implements Serializable {

    @Column(name = "parent_id_1")
    private String id1;
    @Column(name = "parent_id_2")
    private String id2;

    public ParentID_(String id1, String id2){
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
