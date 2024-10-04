package com.example.hotelboard.controller;

import com.example.hotelboard.dto.ArticleForm;
import com.example.hotelboard.entity.Articles;
import com.example.hotelboard.entity.Users;
import com.example.hotelboard.service.ArticleService;
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
    private ArticleService articleService;

    @GetMapping("/articles/new_form")
    public String createNewForm(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("userId", user.getUserId());
            return "/articles/new_form";
        }
        return "redirect:/membership/login";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form, HttpSession session, RedirectAttributes redirectAttributes) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "로그인 후에 게시물을 작성할 수 있습니다.");
            return "redirect:/membership/login";
        }

        Articles savedArticle = articleService.createArticle(form, user);
        if (savedArticle != null && savedArticle.getId() != null) {
            return "redirect:/articles";
        }
        log.error("Failed to save the article.");
        return "redirect:/articles/new_form";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable Long id, Model model) {
        Articles articlesEntity = articleService.getArticleById(id);
        if (articlesEntity == null) {
            log.error("Article with id={} not found.", id);
            return "redirect:/articles";
        }
        model.addAttribute("article", articlesEntity);
        return "articles/showArticle";
    }

    @GetMapping("/articles")
    public String showAll(@RequestParam(required = false) Long articleType, Model model) {
        List<Articles> articlesList = articleService.getAllArticles(articleType);
        model.addAttribute("articlesList", articlesList);
        model.addAttribute("articleType", articleType);
        return "articles/showAllArticles";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Articles editEntity = articleService.getArticleById(id);
        model.addAttribute("article", editEntity);
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(@ModelAttribute ArticleForm form) {
        articleService.updateArticle(form);
        return "redirect:/articles/" + form.getId() + "?articleType=" + form.getArticleType();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return "redirect:/articles";
    }
}
