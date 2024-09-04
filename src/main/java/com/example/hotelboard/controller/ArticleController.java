package com.example.hotelboard.controller;

import com.example.hotelboard.dto.ArticleForm;
import com.example.hotelboard.entity.Articles;
import com.example.hotelboard.entity.Users;
import com.example.hotelboard.repository.ArticleRepository;
import com.example.hotelboard.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    // 새 게시물 생성 폼을 보여주는 메서드
    @GetMapping("/articles/new_form")
    public String createNewForm(Model model, HttpSession session) {
        // 세션에서 사용자 정보를 가져옴
        Users user = (Users) session.getAttribute("user");

        if (user != null) {
            // userId를 모델에 추가하여 폼에서 사용할 수 있도록 함
            model.addAttribute("userId", user.getUserId());
        } else {
            // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/membership/login";
        }

        return "/articles/new_form";
    }

    // 게시물 생성 처리 메서드
    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form, HttpSession session, RedirectAttributes redirectAttributes) {
        // 세션에서 사용자 정보를 가져옴
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "로그인 후에 게시물을 작성할 수 있습니다.");
            return "redirect:/membership/login";
        }

        // DTO to Entity
        Articles articles = new Articles(form.getArticleType(), user, form.getTitle(), form.getContent(), form.getCreatedAt());
        log.info(articles.toString());

        // Entity -> DB : use Repository
        Articles saved = articleRepository.save(articles);
        log.info(saved.toString());

        // Ensure saved ID is not null
        if (saved != null && saved.getId() != null) {
            return "redirect:/articles";
        } else {
            log.error("Failed to save the article.");
            return "redirect:/articles/new_form";
        }
    }

    // 특정 게시물의 상세 정보를 보여주는 메서드
    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable Long id, Model model) {
        log.info("id=" + id);
        Articles articlesEntity = articleRepository.findById(id).orElse(null);

        if (articlesEntity == null) {
            log.error("Article with id={} not found.", id);
            return "redirect:/articles";
        }

        model.addAttribute("article", articlesEntity);
        return "articles/showArticle";
    }

    // 모든 게시물을 리스트로 보여주는 메서드
    @GetMapping("/articles")
    public String showAll(@RequestParam(required = false) Long articleType, Model model) {
        List<Articles> articlesList;

        // articleType이 null이면 모든 게시글을 조회, 그렇지 않으면 특정 타입의 게시글만 조회
        if (articleType == null) {
            articlesList = (List<Articles>) articleRepository.findAll();
        } else {
            articlesList = articleRepository.findByArticleType(articleType);
        }

        model.addAttribute("articlesList", articlesList);
        model.addAttribute("articleType", articleType);

        return "articles/showAllArticles";
    }

    // 게시물 수정 폼을 보여주는 메서드
    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Articles editEntity = articleRepository.findById(id).orElse(null);
        model.addAttribute("article", editEntity);
        return "articles/edit";
    }

    // 게시물 업데이트 처리 메서드
    @PostMapping("/articles/update")
    public String update(@ModelAttribute ArticleForm form) {
        log.info("Received ArticleForm: id={}, title={}, content={}, memberId={}, articleType={}",
                form.getId(), form.getTitle(), form.getContent(), form.getMemberId(), form.getArticleType());

        // UserRepository에서 Users 객체를 조회
        Users user = userRepository.findById(form.getMemberId()).orElse(null);
        if (user == null) {
            log.error("User with id={} not found.", form.getMemberId());
            return "error";
        }

        // 데이터베이스에서 기존 게시물 조회
        Articles dbArticles = articleRepository.findById(form.getId()).orElse(null);
        if (dbArticles != null) {
            // 수정할 Article 엔티티의 정보를 업데이트
            dbArticles.setArticleType(form.getArticleType());
            dbArticles.setMemberId(user);
            dbArticles.setTitle(form.getTitle());
            dbArticles.setContent(form.getContent());
            dbArticles.setCreatedAt(form.getCreatedAt());

            // 변경된 게시물 저장
            articleRepository.save(dbArticles);
        } else {
            log.error("Article with id={} not found in the database.", form.getId());
        }

        return "redirect:/articles/" + form.getId() + "?articleType=" + form.getArticleType();
    }


    // 게시물 삭제 처리 메서드
    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id) {
        log.info("Client article delete request!");
        Articles dbArticles = articleRepository.findById(id).orElse(null);
        if (dbArticles != null) {
            articleRepository.delete(dbArticles);
        }
        return "redirect:/articles";
    }
}
