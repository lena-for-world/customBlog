package projectBlog.customBlog.repository;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.domain.Blog;

@Repository
@RequiredArgsConstructor
public class ArticleRepository {

    private final EntityManager em;

    public Article findArticle(int articleId) {
        return em.find(Article.class, articleId);
    }

    public List<Article> getRecentFiveArticles(Blog blog) {
        return em.createQuery("select a from Article a"
            + " join fetch a.member m" +
            " join fetch a.category c" +
            " where a.member.id = :member_id order by a.id desc", Article.class)
            .setParameter("member_id", blog.getMember().getId())
            .setFirstResult(0)
            .setMaxResults(5)
            .getResultList();
    }

    public List<Article> getPageFiveArticles(Blog blog, int page) {
        return em.createQuery("select a from Article a"
            +" join fetch a.member m" +
            " join fetch a.category c" +
            " where a.member.id = :member_id order by a.id desc", Article.class)
            .setParameter("member_id", blog.getMember().getId())
            .setFirstResult(page)
            .setMaxResults(5)
            .getResultList();
    }

    public List<Article> ArticlesOfCategory(int cateId) {
        return em.createQuery("select a from Article a where a.category.id = :cate_id")
            .setParameter("cate_id", cateId)
            .getResultList();
    }

}
