package com.example.hotelboard.service;

import com.example.hotelboard.dto.ArticleForm;
import com.example.hotelboard.entity.Articles;
import com.example.hotelboard.entity.Users;
import com.example.hotelboard.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;  // ArticleRepository 주입

    @Override
    public Articles createArticle(ArticleForm form, Users user) {
        // 새로운 게시글을 생성하고 데이터베이스에 저장
        Articles article = new Articles(form.getArticleType(), user, form.getTitle(), form.getContent(), new Date());
        return articleRepository.save(article);  // 저장된 게시글 반환
    }

    @Override
    public Articles getArticleById(Long id) {
        // ID로 게시글 조회
        return articleRepository.findById(id).orElse(null);  // 게시글이 없으면 null 반환
    }

    @Override
    public List<Articles> getAllArticles(Long articleType) {
        // 모든 게시글을 조회하거나 특정 유형의 게시글만 조회
        if (articleType == null) {
            return (List<Articles>) articleRepository.findAll();  // 모든 게시글 반환
        } else {
            return articleRepository.findByArticleType(articleType);  // 특정 유형의 게시글 반환
        }
    }

    @Override
    public void updateArticle(ArticleForm form) {
        // 게시글을 업데이트
        Articles dbArticles = articleRepository.findById(form.getId()).orElse(null);  // 데이터베이스에서 게시글 조회
        if (dbArticles != null) {
            // 게시글 정보 업데이트
            dbArticles.setArticleType(form.getArticleType());
            dbArticles.setTitle(form.getTitle());
            dbArticles.setContent(form.getContent());
            dbArticles.setCreatedAt(new Date());  // 수정일자를 현재 시간으로 설정
            articleRepository.save(dbArticles);  // 업데이트된 게시글 저장
        } else {
            log.error("Article with id={} not found in the database.", form.getId());  // 게시글이 없을 경우 오류 로그
        }
    }

    @Override
    public void deleteArticle(Long id) {
        // 게시글 삭제
        Articles dbArticles = articleRepository.findById(id).orElse(null);  // 데이터베이스에서 게시글 조회
        if (dbArticles != null) {
            articleRepository.delete(dbArticles);  // 게시글 삭제
        }
    }
}
