package aisl.example1.service;

import aisl.example1.domain.User;
import aisl.example1.repository.UserRepository;

import java.util.List;
import java.util.Optional;
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 로그인
     */

    public User login(String email, String password) {
        Optional<User> findMember = userRepository.findByEmail(email);
        if(!findMember.orElseThrow(()-> new NotCorrespondingEmailException("해당 이메일이 존재하지 않습니다.")).checkPassword(password)){
            throw new IllegalStateException("이메일과 비밀번호가 일치하지 않습니다.");
        }
        return findMember.get();
    }

    /**
     * 회원가입
     */

    public int join(User user) {
        validateDuplicateMember(user); // 중복 회원 검증
        userRepository.save(user);
        return user.getUserid();
    }

    private void validateDuplicateMember(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체 회원조회
     */
    public List<User> findMembers() {
        return userRepository.findAll();
    }

    public Optional<User> findOne(String name) {
        return userRepository.findByName(name);
    }
}