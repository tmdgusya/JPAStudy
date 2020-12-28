package Relation3;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Getter
@Setter
@DiscriminatorValue("M")
@PrimaryKeyJoinColumn(name = "movie_id")
public class Movie extends Items {

    private String director;
    private String actor;
}
