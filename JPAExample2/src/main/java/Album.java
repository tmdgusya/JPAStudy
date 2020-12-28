import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Getter
@Setter
@DiscriminatorValue("A")
public class Album extends Item{

    private String artist;

    @Lob()
    private String etc;
}
