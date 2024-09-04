package com.example.hotelboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(unique = true) // userId를 유니크하게 설정
    private String userId;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String phoneNum;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    // 기존 생성자에 Role 기본값 설정
    public Users(Long memberId, String name, String userId, String password, String phoneNum, Role role) {
        this.memberId = memberId;
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.phoneNum = phoneNum;
        this.role = Role.USER;  // 기본값 설정
    }

    // Role을 포함하는 생성자
    public Users(String name, String userId, String password, String phoneNum, Role role) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.phoneNum = phoneNum;
        this.role = role != null ? role : Role.USER;  // Role이 null일 경우 USER로 설정
    }

    public Users(Long memberId) {

    }


    // Role Enum 타입 정의
    public enum Role {
        USER, ADMIN
    }
}
