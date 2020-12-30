import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

// 값 타입은 Immutable 하게
@Embeddable
@Getter
public class Address {

    public Address(){};

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    private String city;
    private String street;
    private String zipcode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(this.city, address.city) &&
                Objects.equals(this.street, address.street) &&
                Objects.equals(this.zipcode, address.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.city, this.street, this.zipcode);
    }
}
