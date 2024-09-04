package com.example.hotelboard.dto;

import com.example.hotelboard.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class UsersForm {
    private Long memberId;
    private String name;
    private String userId;
    private String password;
    private String phoneNum;
    private Users.Role role;

    public Users toEntity() {
        return new Users(memberId, name, userId, password, phoneNum, role);
    }
}
