package Relation2;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @Column(name = "product_id")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "products")
    private List<Dodo> member = new ArrayList<>();
}
