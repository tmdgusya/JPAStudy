package Relation2;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Dodo {

    @Id
    @Column(name = "dodo_id")
    private Long id;

    private String username;

    @ManyToMany
    @JoinTable(name = "member_product",
    joinColumns = @JoinColumn(name = "dodo_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<Product>();
}
