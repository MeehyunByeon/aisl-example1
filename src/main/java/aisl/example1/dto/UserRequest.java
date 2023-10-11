package aisl.example1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private Long id;

    private String account;

    private String name;

    private String email;

    private String password;
}
