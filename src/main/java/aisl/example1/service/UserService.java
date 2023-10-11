package aisl.example1.service;

import aisl.example1.domain.Authority;
import aisl.example1.domain.Token;
import aisl.example1.domain.User;
import aisl.example1.dto.TokenDto;
import aisl.example1.dto.UserRequest;
import aisl.example1.dto.UserResponse;
import aisl.example1.repository.TokenRepository;
import aisl.example1.repository.UserRepository;
import aisl.example1.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    public UserResponse login(UserRequest request) throws Exception {
        User user = userRepository.findByAccount(request.getAccount()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        user.setRefreshToken(createRefreshToken(user));

        return UserResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .email(user.getEmail())
                .name(user.getName())
                .roles(user.getRoles())
                .token(TokenDto.builder()
                        .access_token(jwtProvider.createToken(user.getAccount(), user.getRoles()))
                        .refresh_token(user.getRefreshToken())
                        .build())
                .build();
    }

    public boolean join(UserRequest request) throws Exception {
        try {
            User user = User.builder()
                    .account(request.getAccount())
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();
            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
            userRepository.save(user);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    public UserResponse getUser(String account) throws Exception {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
        return new UserResponse(user);
    }

    public String createRefreshToken(User user) {
        System.out.println("1111111111111");
        Token token = tokenRepository.save(
                Token.builder()
                        .id(user.getId())
                        .refresh_token(UUID.randomUUID().toString())
                        .expiration(120) // 2분ㅋ
                        .build()
        );
        System.out.println("token" + token.getRefresh_token());
        return token.getRefresh_token();
    }

    public Token validRefreshToken(User user, String refreshToken) throws Exception {
        Token token = tokenRepository.findById(user.getId()).orElseThrow(() -> new Exception("만료된 계정입니다. 로그인을 다시 시도하세요"));
        // 해당 유저 Refresh 토큰 만료 => Redis에 해당 유저의 토큰이 존재하지 않음
        if(token.getRefresh_token() == null) {
            return null;
        }
        else {
            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장
            if(token.getExpiration() < 10) {
                token.setExpiration(1000);
                tokenRepository.save(token);
            }

            // 토큰이 같은지 비교
            if(!token.getRefresh_token().equals(refreshToken)) {
                return null;
            }
            else {
                return token;
            }
        }
    }

    public TokenDto refreshAccessToken(TokenDto token) throws Exception {
        String account = jwtProvider.getAccount(token.getAccess_token());
        User user = userRepository.findByAccount(account).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정정보입니다."));
        Token refreshToken = validRefreshToken(user, token.getRefresh_token());

        if (refreshToken != null) {
            return TokenDto.builder()
                    .access_token(jwtProvider.createToken(account, user.getRoles()))
                    .refresh_token(refreshToken.getRefresh_token())
                    .build();
        }
        else {
            throw new Exception("로그인을 해주세요");
        }
    }
}