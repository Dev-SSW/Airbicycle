package com.example.Airbicycle.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Entity @Getter @Setter
public class User implements UserDetails, OAuth2User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
<<<<<<< HEAD
    @Column(unique = true)
    private String username; // 아이디
    private String password;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private Role role;
    // 현재 자전거 빌림 여부  false -> 안빌림 /  true -> 빌림
    private boolean hasRented = false;
=======
    private String username; //ID
    private String password; //Password
    private String name;     //사용자 이름
    private String email;    //이메일
    private String phone;    //휴대폰
    private String address;  //주소
    private String nickname; //닉네임
    @Enumerated(EnumType.STRING)
    private Role role;       //역할
>>>>>>> feature/login

    //UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

<<<<<<< HEAD
    public boolean gethasRented() {
        return this.hasRented;
=======
    //OAuth2
    private String profileImage; //프로필 이미지(OAuth2)

    @Transient  // JPA가 이 필드를 데이터베이스에 매핑하지 않도록 설정
    private Map<String, Object> attributes; //OAuth2 속성

    // OAuth2User 인터페이스의 메서드 구현
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsername() {
        return username;
>>>>>>> feature/login
    }
}
