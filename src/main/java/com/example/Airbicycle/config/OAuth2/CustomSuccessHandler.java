package com.example.Airbicycle.config.OAuth2;

import com.example.Airbicycle.config.JWTUtils;
import com.example.Airbicycle.domain.User;
import com.example.Airbicycle.dto.JwtResponse;
import com.example.Airbicycle.repository.UserRepository;
import com.example.Airbicycle.service.AuthService;
import com.example.Airbicycle.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;
    //http://localhost:8080/oauth2/authorization/google
    //http://localhost:8080/oauth2/authorization/naver
    //http://localhost:8080/oauth2/authorization/kakao
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // OAuth2User로 캐스팅
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String provider = request.getRequestURI().contains("google") ? "google" : "naver";
        String username = null;
        System.out.println("provider : " + provider);
        if ("google".equals(provider)) {
            // 구글의 경우 'sub'을 사용
            username = (String) oAuth2User.getAttributes().get("sub");
        } else if ("naver".equals(provider)) {
            // 네이버의 경우 'id'를 사용 (response 안에 있음)
            // 네이버의 경우 'response' 안에 'id'를 사용
            Map<String, Object> attributes = oAuth2User.getAttributes();
            username = (String) oAuth2User.getAttributes().get("id");
        }
        // 사용자 정보 조회
        if (username == null) {
            throw new RuntimeException(new Exception("OAuth 사용자 ID를 찾을 수 없습니다."));
        }

        // username으로 User 엔티티 찾기 (provider 정보를 포함하여 조회)
        System.out.println("사용자를 찾고 있습니다. (provider + id): " + provider + "_" + username);
        User users = userRepository.findByUsername(provider + "_" + username).orElseThrow(() ->
                new RuntimeException(new Exception("사용자를 찾을 수 없습니다.")));

        // JWT 및 Refresh Token 처리
        String accessToken = jwtUtils.generateToken(users);
        String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), users);

        // JWT를 헤더에 추가
        response.addHeader("Authorization", "Bearer " + accessToken);

        // Refresh Token을 쿠키에 추가
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);  // XSS 공격 방지
        refreshTokenCookie.setSecure(true);  // HTTPS에서만 전송
        refreshTokenCookie.setPath("/");  // 전체 경로에서 사용 가능
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);  // 7일 동안 유효
        response.addCookie(refreshTokenCookie);

        // 로그인 성공 후 리다이렉트할 URL 결정
        String targetUrl = determineTargetUrl(request, response);

        // 리다이렉트 처리
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        return "/";  // 기본적으로 홈 페이지로 리다이렉트
    }
}
