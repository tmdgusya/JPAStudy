package MappedSuperClass;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "seller_id")),
        @AttributeOverride(name = "name", column = @Column(name = "seller_name"))
})
public class Seller extends BaseEntity{

    private String shopName;
}
