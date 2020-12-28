package Relation3;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Getter
@Setter
@DiscriminatorValue("A")
@PrimaryKeyJoinColumn(name = "album_id")
public class Album extends Items {

}
