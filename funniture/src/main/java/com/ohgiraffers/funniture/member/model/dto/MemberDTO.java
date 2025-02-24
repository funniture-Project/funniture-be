package com.ohgiraffers.funniture.member.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MemberDTO implements UserDetails {

    private String memberId;

    private String memberRole;

    private String email;

    private String password;

    private String userName;

    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime signupDate;

    private int isConsulting;

    private int hasImage;

    private String imageLink;

    private String imageId;
    private Collection<GrantedAuthority> authorities;

    // 25-02-24, 사용자 기본 주소 추가
    private String address;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (memberRole != null) {
            // memberRole이 쉼표로 구분된 String이라고 가정
             String[] roles = memberRole.split(","); // 쉼표로 역할을 나눔
//            authorities.add(() -> memberRole); // 이게 맞나?

            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.trim())); // 각 역할을 GrantedAuthority로 변환
            }
            return authorities;
        }
        return new ArrayList<>();
    }



    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}