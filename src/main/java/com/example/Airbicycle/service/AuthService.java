package com.example.Airbicycle.service;

import com.example.Airbicycle.config.JWTUtils;
import com.example.Airbicycle.domain.User;
import com.example.Airbicycle.dto.JwtRequest;
import com.example.Airbicycle.dto.JwtResponse;
import com.example.Airbicycle.dto.SigninRequest;
import com.example.Airbicycle.dto.SignupRequest;
import com.example.Airbicycle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.airbicycle.config.exception.UsernameAlreadyExistsException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    //회원가입
    public JwtResponse signUp(SignupRequest request){
        JwtResponse resp = new JwtResponse();
        try {
            //이름 중복 검사
            userRepository.findByUsername(request.getUsername())
                    .ifPresent(u -> {throw new UsernameAlreadyExistsException("이미 존재하는 아이디입니다.");});
            //USER 생성
            User users = new User();
            users.setUsername(request.getUsername());
            users.setPassword(passwordEncoder.encode(request.getPassword()));
            users.setName(request.getName());
            users.setEmail(request.getEmail());
            users.setPhone(request.getPhone());
            users.setAddress(request.getAddress());
            users.setNickname(request.getNickname());
            users.setRole(request.getRole());

            User userResult = userRepository.save(users);
            if (userResult != null && userResult.getId()>0) {
                resp.setUsers(userResult);
                resp.setMessage("회원 저장 성공");
                resp.setStatusCode(200);
            }
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    //로그인
    public JwtResponse signIn(SigninRequest signinRequest){
        JwtResponse resp = new JwtResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUsername(),signinRequest.getPassword()));
            var user = userRepository.findByUsername(signinRequest.getUsername()).orElseThrow();
            System.out.println("USER IS: "+ user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            resp.setStatusCode(200);
            resp.setToken(jwt);
            resp.setNickname(user.getNickname());
            resp.setRefreshToken(refreshToken);
            resp.setExpirationTime("24Hr");
            resp.setMessage("로그인 성공");
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    //리프레쉬 토큰
    public JwtResponse refreshToken(JwtRequest refreshTokenRequest){
        JwtResponse response = new JwtResponse();
        try {
            String ourName = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User users = userRepository.findByUsername(ourName).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("재발급 성공");
            } else {
                response.setStatusCode(401);
                response.setError("유효하지 않은 토큰");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    //토큰 유효성 검사
    public JwtResponse validateToken(JwtRequest validateTokenRequest) {
        JwtResponse response = new JwtResponse();
        try {
            String ourName = jwtUtils.extractUsername(validateTokenRequest.getToken());
            User user = userRepository.findByUsername(ourName).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
            // 토큰 유효성 검사
            if (jwtUtils.isTokenValid(validateTokenRequest.getToken(), user)) {
                response.setStatusCode(200); // OK
                response.setMessage("토큰이 유효합니다.");
            } else {
                response.setStatusCode(401); // Unauthorized
                response.setMessage("유효하지 않은 토큰입니다.");
            }
        } catch (Exception e) {
            // 예외 발생 시
            response.setStatusCode(500); // Internal Server Error
            response.setError(e.getMessage());
        }
        return response;
    }
}
