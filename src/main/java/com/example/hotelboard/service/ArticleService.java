package com.example.hotelboard.service;

import com.example.hotelboard.dto.ArticleForm;
import com.example.hotelboard.entity.Articles;
import com.example.hotelboard.entity.Users;

import java.util.List;

public interface ArticleService {
    Articles createArticle(ArticleForm form, Users user);
    Articles getArticleById(Long id);
    List<Articles> getAllArticles(Long articleType);
    void updateArticle(ArticleForm form);
    void deleteArticle(Long id);
}
