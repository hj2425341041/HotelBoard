package com.example.hotelboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

// 기본 생성자를 생성하는 Lombok 어노테이션
@NoArgsConstructor
// JPA 엔티티를 정의하는 어노테이션
@Entity
// 객체의 문자열 표현을 생성하는 Lombok 어노테이션
@ToString
// getter 메서드를 자동으로 생성하는 Lombok 어노테이션
@Getter
// setter 메서드를 자동으로 생성하는 Lombok 어노테이션
@Setter
public class Articles {
    // 기본 키를 설정하고, 자동 생성 전략을 설정하는 어노테이션
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 컬럼을 매핑하는 어노테이션
    @Column
    private Long articleType;

    // ManyToOne 관계를 설정하며, 외래 키를 정의하는 어노테이션
    @ManyToOne
    @JoinColumn(name = "member_id") // 외래 키 설정
    private Users memberId; // 사용자의 정보와 연결된 외래 키

    // 게시물 제목을 저장하는 컬럼
    @Column
    private String title;

    // 게시물 내용을 저장하는 컬럼
    @Column
    private String content;

    // 게시물 생성 시간을 저장하는 컬럼
    @Column
    private Date createdAt = new Date();

    // 새로운 Article을 생성할 때 사용되는 생성자
    public Articles(Long articleType, Users memberId, String title, String content, Date date) {
        this.articleType = articleType;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.createdAt = new Date();
    }

    // 기존 Article을 수정할 때 사용되는 생성자
    public Articles(Long id, Long articleType, Users memberId, String title, String content, Date date) {
        this.id = id;
        this.articleType = articleType;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.createdAt = new Date();
    }

    // 기존의 Article을 업데이트할 때 사용되는 메서드
    public void patch(Articles articles) {
        // 새로운 제목이 null이 아닌 경우 업데이트
        if (articles.title != null)
            this.title = articles.title;

        // 새로운 내용이 null이 아닌 경우 업데이트
        if (articles.content != null)
            this.content = articles.content;

        // 새로운 게시글 타입이 null이 아닌 경우 업데이트
        if (articles.articleType != null)
            this.articleType = articles.articleType;

//        // 새로운 날짜가 null이 아닌 경우 업데이트
//        if (articles.createdAt != null)
//            this.createdAt = articles.createdAt;
    }

}
