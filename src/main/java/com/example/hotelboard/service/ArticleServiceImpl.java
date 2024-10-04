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
    private ArticleRepository articleRepository;

    @Override
    public Articles createArticle(ArticleForm form, Users user) {
        Articles article = new Articles(form.getArticleType(), user, form.getTitle(), form.getContent(), new Date());
        return articleRepository.save(article);
    }

    @Override
    public Articles getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Articles> getAllArticles(Long articleType) {
        if (articleType == null) {
            return (List<Articles>) articleRepository.findAll();
        } else {
            return articleRepository.findByArticleType(articleType);
        }
    }

    @Override
    public void updateArticle(ArticleForm form) {
        Articles dbArticles = articleRepository.findById(form.getId()).orElse(null);
        if (dbArticles != null) {
            dbArticles.setArticleType(form.getArticleType());
            dbArticles.setTitle(form.getTitle());
            dbArticles.setContent(form.getContent());
            dbArticles.setCreatedAt(new Date());
            articleRepository.save(dbArticles);
        } else {
            log.error("Article with id={} not found in the database.", form.getId());
        }
    }

    @Override
    public void deleteArticle(Long id) {
        Articles dbArticles = articleRepository.findById(id).orElse(null);
        if (dbArticles != null) {
            articleRepository.delete(dbArticles);
        }
    }
}
