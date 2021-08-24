package projectBlog.customBlog.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.repository.ArticleRepository;
import projectBlog.customBlog.repository.CrudRepository;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CrudRepository crudRepository;


    public void editArticle(int articleId, String title, String content) {
        Article article = articleRepository.findArticle(articleId);
        article.editContent(title, content);
    }

    public void saveArticle(int articleId) {
        Article article = articleRepository.findArticle(articleId);
        crudRepository.save(article);
    }

    public void deleteArticle(int articleId) {
        Article article = articleRepository.findArticle(articleId);
        crudRepository.delete(article);
    }


}
