package aisl.example1.controller;

import aisl.example1.domain.User;
import aisl.example1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
        System.out.println("userService = " + userService.getClass());
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "loginform";
    }

    @PostMapping("/login")
    public String login(UserForm form) {
        User user = new User();
        user.setEmail(form.getEmail());
        user.setPwd(form.getPwd());

        userService.login(user);

        return "redirect:/";
    }
}
