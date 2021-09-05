package projectBlog.customBlog.controller;

import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.domain.Blog;
import projectBlog.customBlog.domain.Category;
import projectBlog.customBlog.domain.Comment;
import projectBlog.customBlog.domain.Member;
import projectBlog.customBlog.dto.ArticleForm;
import projectBlog.customBlog.dto.LoginForm;
import projectBlog.customBlog.repository.ArticleRepository;
import projectBlog.customBlog.repository.BlogRepository;
import projectBlog.customBlog.repository.CategoryRepository;
import projectBlog.customBlog.service.ArticleService;
import projectBlog.customBlog.service.BlogService;
import projectBlog.customBlog.service.CategoryService;
import projectBlog.customBlog.service.CommentService;
import projectBlog.customBlog.validation.ArticleValidator;
import projectBlog.customBlog.validation.CategoryValidator;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BlogController {

    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final CategoryValidator categoryValidator;
    private final BlogService blogService;
    private final BlogRepository blogRepository;
    private final CommentService commentService;
    private final ArticleService articleService;
    private final ArticleValidator articleValidator;

    @GetMapping("/blog/{blogId}")
    public String getBlog(@PathVariable("blogId") int blogId, Model model, HttpServletRequest request) {
        List<Article> articles = blogService.getRecentFiveArticlesOfBlog(blogId);
        List<Category> categories = categoryRepository.getAllCategoriesOfBlog(blogId);
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        return "blog/blogMain";
    }


    @GetMapping("/blog/write/{blogId}")
    public String getArticleForm(@PathVariable("blogId") int blogId, @ModelAttribute("articleForm") ArticleForm form, Model model) {
        List<Category> categories = categoryRepository.getAllCategoriesOfBlog(blogId);
        model.addAttribute("blogId", blogId);
        model.addAttribute("categories", categories);
        return "article/articleForm";
    }


    @PostMapping("/blog/write/{blogId}")
    public String postArticle(@PathVariable("blogId") int blogId, @ModelAttribute("articleForm") ArticleForm form, BindingResult bindingResult) {

        articleValidator.validate(form, bindingResult);

        if(bindingResult.hasErrors()) {
            log.info("{}", bindingResult);
            return "article/articleForm";
        }

        Member member = blogRepository.findBlog(blogId).getMember();
        Category category = categoryRepository.findCategory(form.getCategoryId());
        Article article = Article.makeArticle(form.getTitle(), form.getContent(), LocalDateTime.now(), member, category);
        articleService.saveArticle(article);
        return "redirect:/"; /** */
    }

    @PostMapping("/comment/parent/{articleId}")
    public String postComment(@PathVariable("articleId") int articleId, String content) {

        commentService.postParentComment(articleId, content);

        return "redirect:/"; /** */
    }


    @PostMapping("/blog/delete/{blogId}/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") int categoryId, BindingResult bindingResult) {

        Category category = categoryRepository.findCategory(categoryId);

        categoryValidator.validate(category, bindingResult);

        if(bindingResult.hasGlobalErrors()) {
            log.info("{}", bindingResult);
            return "/blog/{blogId}";
        }

        categoryService.categoryDelete(categoryId);

        return "/blog/{blogId}";
    }

}