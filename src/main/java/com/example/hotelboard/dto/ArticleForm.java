package com.example.hotelboard.dto;

import com.example.hotelboard.entity.Articles;
import com.example.hotelboard.entity.Users;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

// DTO (Data Transfer Object) 클래스
@Getter
@Setter
public class ArticleForm {
    // 게시물 ID
    private Long id;
    // 게시물 유형
    private Long articleType;
    // 사용자 ID (외래 키로 사용)
    private Long memberId;
    // 게시물 제목
    private String title;
    // 게시물 내용
    private String content;
    // 게시물 생성 시간
    private Date createdAt;


    // DTO 객체를 Articles 엔티티로 변환하는 메서드
    public Articles toEntity() {
        // Articles 엔티티를 생성하여 반환
        return new Articles(id, articleType, new Users(memberId), title, content, new Date());
        // Users 객체를 생성할 때 memberId를 사용
        // createdAt에는 현재 날짜를 설정
    }

//    public Articles toEntity(Users user) {
//        return new Articles(id, articleType, user, title, content, new Date());
//    }
}
