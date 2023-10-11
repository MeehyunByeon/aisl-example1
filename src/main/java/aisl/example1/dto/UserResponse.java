package aisl.example1.dto;

import aisl.example1.domain.Authority;
import aisl.example1.domain.User;
import aisl.example1.dto.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;

    private String account;

    private String name;

    private String email;

    private String password;

    private List<Authority> roles = new ArrayList<>();

    private TokenDto token;

    public UserResponse(User user) {
        this.id = user.getId();
        this.account = user.getAccount();
        this.name = user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
