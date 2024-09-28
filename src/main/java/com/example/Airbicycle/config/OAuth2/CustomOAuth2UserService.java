package com.example.Airbicycle.config.OAuth2;

import com.example.Airbicycle.config.OAuth2dto.GoogleResponse;
import com.example.Airbicycle.config.OAuth2dto.KakaoResponse;
import com.example.Airbicycle.config.OAuth2dto.NaverResponse;
import com.example.Airbicycle.config.OAuth2dto.OAuth2Response;
import com.example.Airbicycle.domain.Role;
import com.example.Airbicycle.domain.User;
import com.example.Airbicycle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //제공자 이름 (Ex. google)
        String provider = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("provider의 이름은 : " + provider);
        OAuth2Response oAuth2Response = null;
        Map<String, Object> attributes;
        String nameAttributeKey;

        // 제공자별 분기 처리
        if("google".equals(provider)) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
            attributes = oAuth2User.getAttributes();
            nameAttributeKey = "name";  // 구글은 name 필드를 사용
        } else if("naver".equals(provider)) {
            // 네이버의 경우 response 내부 값을 추출하여 처리
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
            attributes = (Map<String, Object>) oAuth2User.getAttributes().get("response");  // 네이버는 response 내부에 정보가 있음
            nameAttributeKey = "id";  // 네이버는 id 필드를 사용
        } else {
            return null;
        }

        // 사용자 명을 제공자_회원아이디 형식으로만들어 저장
        String username = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

        // 추가된 로그: 사용자가 저장되기 전에 확인
        System.out.println("만들어진 사용자 명은 : " + username);

        //회원 정보가 이미 테이블에 존재하는지 확인 (중복 계정)
        User existData = userRepository.findUsername(username);
        User users;
        if(existData == null) {
            users = new User();
            users.setUsername(username);
            users.setName(oAuth2Response.getName());
            users.setEmail(oAuth2Response.getEmail());
            users.setProfileImage(oAuth2Response.getProfileImage());
            users.setRole(Role.ROLE_USER);
            userRepository.save(users);

            // 추가된 로그: 새로운 사용자가 저장되었을 때
            System.out.println("새로운 유저가 가입했습니다. 유저 이름은 : " + users.getUsername());
        }
        else {
            // 회원정보가 존재한다면 조회된 데이터로 반환한다 + 속성 업데이트
            users = new User();
            users.setUsername(username);
            users.setName(existData.getName());
            users.setEmail(existData.getEmail());
            users.setProfileImage(existData.getProfileImage());
            users.setRole(existData.getRole());

            // 추가된 로그: 기존 사용자 정보 출력
            System.out.println("기존 유저를 찾았습니다. 유저 이름은 : " + users.getUsername());
        }

        System.out.println("getAuthorities : " + oAuth2User.getAuthorities() + " attributes : " + attributes + " key : " + nameAttributeKey);
        // OAuth2User 반환, attributes에 User 엔티티 정보 포함

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                attributes,  // 구글과 네이버의 attributes를 다르게 전달
                nameAttributeKey  // 반환할 사용자의 고유 식별자 필드
        );
    }
}
