package aisl.example1.controller;

import aisl.example1.dto.TokenDto;
import aisl.example1.dto.UserRequest;
import aisl.example1.dto.UserResponse;
import aisl.example1.repository.UserRepository;
import aisl.example1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) throws Exception {
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }

    @PostMapping(value = "/join")
    public ResponseEntity<Boolean> join(@RequestBody UserRequest request) throws Exception {
        return new ResponseEntity<>(userService.join(request), HttpStatus.OK);
    }

    @GetMapping("/user/get")
    public ResponseEntity<UserResponse> getUser(@RequestParam String account) throws Exception {
        return new ResponseEntity<>(userService.getUser(account), HttpStatus.OK);
    }

    @GetMapping("/admin/get")
    public ResponseEntity<UserResponse> getUserForAdmin(@RequestParam String account) throws Exception {
        return new ResponseEntity<>(userService.getUser(account), HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto token) throws Exception {
        return new ResponseEntity<>(userService.refreshAccessToken(token), HttpStatus.OK);
    }
}
