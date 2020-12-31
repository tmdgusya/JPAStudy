import lombok.Getter;

@Getter
public class UserDTO {

    private String username;
    private int age;

    public UserDTO(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
