package projectBlog.customBlog.repository;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import projectBlog.customBlog.domain.Article;

@Repository
@RequiredArgsConstructor
public class ArticleRepository {

    private final EntityManager em;

    public Article findArticle(int articleId) {
        return em.find(Article.class, articleId);
    }

    public List<Article> findRecentFiveArticles() {
        return em.createQuery("select a from Article a" +
            " join fetch a.member m" +
            " join fetch a.category c" +
            " order by a.id desc", Article.class)
            .setFirstResult(0)
            .setMaxResults(5)
            .getResultList();
    }

}
