package MappedSuperClass;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "guest_id")),
        @AttributeOverride(name = "name", column = @Column(name = "guest_name"))
})

public class Guest extends BaseEntity{

    private String email;
}
