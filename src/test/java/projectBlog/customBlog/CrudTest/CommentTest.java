package projectBlog.customBlog.CrudTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import projectBlog.customBlog.Domain.Article;
import projectBlog.customBlog.Domain.Category;
import projectBlog.customBlog.Domain.Comment;
import projectBlog.customBlog.Domain.Member;

@DataJpaTest
@Transactional
public class CommentTest {

    @Autowired
    private EntityManager em;
    String title, content;
    Member member;
    Category category;
    Article article;

    public void save(Object object) {
        em.persist(object);
    }

    public void delete(Object object) {
        em.remove(object);
    }

    @BeforeEach
    void beforeEach() {
        title = "black widow";
        content = "content";
        member = new Member();
        category = Category.makeParentCategory("category");
        article = Article.makeArticle(title, content, LocalDateTime.now(), member, category);
        save(category);
        save(member);
        save(article);
    }

    @Test
    @DisplayName("댓글 등록")
    public void PostComment() {
        // when
        Comment comment = Comment.makeParentComment("댓글!!");
        article.addCommentToArticle(comment);
        comment.setCommentArticle(article);
        save(comment);

        // then
        Article article = em.createQuery("select a from Article a", Article.class).getResultList().get(0);
        assertEquals(article.getComment(), comment.getContent());
        System.out.println("article.getComment() = " + article.getComment());

        comment = em.find(Comment.class, comment.getId());
        System.out.println(comment.getArticle().getContent());

    }

    @Test
    @DisplayName("게시글의 댓글 전체 조회")
    public void findAllCommentsForArticle() {

        Article article2 = Article.makeArticle(title, content, LocalDateTime.now(), member, category);
        save(article2);

        Comment comment = Comment.makeParentComment("댓글!!");
        article.addCommentToArticle(comment);
        comment.setCommentArticle(article);
        save(comment);

        Comment comment2 = Comment.makeParentComment("댓글!!2222");
        article.addCommentToArticle(comment2);
        comment2.setCommentArticle(article);
        save(comment2);

        Comment comment3 = Comment.makeParentComment("댓글!!1-1");
        article2.addCommentToArticle(comment3);
        comment3.setCommentArticle(article2);
        save(comment3);

        List<Comment> comments = em.createQuery("select a from Comment a")
            .getResultList();
        assertEquals(comments.size(), 3);

        List<Article> articles = em.createQuery("select a from Article a", Article.class).getResultList();
        for(Article article : articles) {
            comments = article.getComments();
            for(Comment com : comments) {
                System.out.println("comment = " + com.getContent());
            }
        }

    }

}
