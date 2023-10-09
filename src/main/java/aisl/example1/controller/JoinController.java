package aisl.example1.controller;

import aisl.example1.domain.User;
import aisl.example1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JoinController {

    private final UserService userService;

    @Autowired
    public JoinController(UserService userService) {
        this.userService = userService;
        System.out.println("userService = " + userService.getClass());
    }

    @GetMapping("/join")
    public String showJoinForm() {
        return "joinform";
    }

    @PostMapping("/join")
    public String join(UserForm form) {
        User user = new User();
        user.setEmail(form.getEmail());

        userService.join(user);

        return "redirect:/";
    }

}
