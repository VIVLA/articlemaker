package com.vivla.articlemaker.controllers;

import com.vivla.articlemaker.entities.ArticleEntity;
import com.vivla.articlemaker.repositories.ArticleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "Article generator");
        return "home";
    }

    @GetMapping("/home")
    public String homeAgain(Model model) {
        model.addAttribute("name", "Article generator");
        return "home";
    }

    @GetMapping("/articles")
    public String article(Model model) {
        model.addAttribute("name", "Articles");
        Iterable<ArticleEntity> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "articles";
    }

    @GetMapping("/add")
    public String addArticleGet(Model model) {
        model.addAttribute("name", "Add articles");
        return "add";
    }

    @PostMapping("/add")
    public String addArticlePost(@RequestParam String title,
                                 @RequestParam String anons,
                                 @RequestParam String text) {
        ArticleEntity articleEntity = new ArticleEntity(title, anons, text);
        articleRepository.save(articleEntity);
        return "redirect:/articles";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("name", "About Articles generator");
        return "aboutArticles";
    }

    @GetMapping("/articleDetails/{id}")
    public String articleDetailsGet(@PathVariable(value = "id") Long id, Model model) {
        getArticleById(id);
        Optional<ArticleEntity> article = articleRepository.findById(id);
        ArrayList<ArticleEntity> result = new ArrayList<>();
        article.ifPresent(result::add);
        model.addAttribute("article", result);
        model.addAttribute("name", "Article reading");
        return "articleDetails";
    }

    @PostMapping("/articleDetails/{id}")
    public String articleDetailsPost(@PathVariable(value = "id") Long id, Model model) {
        ArticleEntity article = articleRepository.findById(id).orElseThrow();
        articleRepository.delete(article);
        model.addAttribute("name", "Article reading");
        return "redirect:/articles";
    }

    private String getArticleById(Long id) {
        return !articleRepository.existsById(id) ? "redirect:/home" : "";
    }

    @GetMapping("/articleDetails/{id}/edit")
    public String articleEditGet(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("name", "Article editing");
        Optional<ArticleEntity> article = articleRepository.findById(id);
        ArrayList<ArticleEntity> result = new ArrayList<>();
        article.ifPresent(result::add);
        model.addAttribute("editingArticle", result);
        return "edit";
    }

    @PostMapping("/articleDetails/{id}/edit")
    public String articleEditPost(@PathVariable(value = "id") Long id,
                                  @RequestParam String title,
                                  @RequestParam String anons,
                                  @RequestParam String text) {
        ArticleEntity articleEntity = articleRepository.findById(id).orElseThrow();
        articleEntity.setTitle(title);
        articleEntity.setAnons(anons);
        articleEntity.setText(text);
        articleRepository.save(articleEntity);
        return "redirect:/articleDetails/{id}";
    }
}
