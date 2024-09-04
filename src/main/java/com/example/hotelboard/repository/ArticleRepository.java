package com.example.hotelboard.repository;

import com.example.hotelboard.entity.Articles;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface ArticleRepository extends CrudRepository<Articles, Long> {

    @Override
    ArrayList<Articles> findAll(); //모든 override는 가져와서 내가 원하는 형태로 변환 후 사용할 수 있음

    List<Articles> findByArticleType(Long articleType);// 특정 articleType을 가진 게시글을 조회하는 메서드
}
