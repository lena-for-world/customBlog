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
    Comment comment;

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

        comment = Comment.makeParentComment("게시물 1의 댓글!!");
        article.addCommentToArticle(comment);
        comment.setCommentArticle(article);
        save(comment);


    }

    @Test
    @DisplayName("댓글 등록")
    public void PostComment() {

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

        Comment comment2 = Comment.makeParentComment("게시물 2의 댓글!!2222");
        article2.addCommentToArticle(comment2);
        comment2.setCommentArticle(article2);
        save(comment2);

        Comment comment3 = Comment.makeParentComment("게시물 1의 댓글!!3333");
        article.addCommentToArticle(comment3);
        comment3.setCommentArticle(article);
        save(comment3);

        Comment comment4 = Comment.makeChildComment("게시물 1의 댓글 1의 대댓글!!4444");
        try {
            comment.addChildComment(comment4);
        } catch(Exception e) {

        }
        save(comment4);

        List<Comment> comments = em.createQuery("select a from Comment a")
            .getResultList();
        assertEquals(comments.size(), 4);

        List<Article> articles = em.createQuery("select a from Article a", Article.class).getResultList();
        for(Article article : articles) {
            comments = article.getComments();
            for(Comment com : comments) {
                System.out.println("comment = " + com.getContent());
                for(Comment child : com.getChilds()) {
                    System.out.println("child comment = " + child.getContent());
                }
            }
        }

    }

    @Test
    @DisplayName("부모 댓글에 부모 댓글 생성 불가")
    public void noParentforParent() {
        Comment comment2 = Comment.makeParentComment("부모댓글!!2222");
        try {
            comment.addChildComment(comment2);
            fail();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("자식 댓글 아래에 부모 댓글 생성 불가")
    public void childHasNoParent() {
        // given
        Comment comment2 = Comment.makeChildComment("자식댓글!!");
        try {
            // when
            comment2.addChildComment(comment);
            fail();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    @DisplayName("자식 댓글 아래에 자식 댓글 생성 불가")
    public void childHasNoChild() {
        // given
        Comment comment2 = Comment.makeChildComment("자식댓글!!");
        Comment comment3 = Comment.makeChildComment("자식댓글!!22");
        try {
            // when
            comment2.addChildComment(comment3);
            fail();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    @DisplayName("부모 댓글 아래에 자식 댓글 생성")
    public void parentCategoryHasChildCategory() {

        Comment comment2 = Comment.makeChildComment("자식댓글!!");
        try {
            comment.addChildComment(comment2);
        } catch(Exception e) {
            e.printStackTrace();
        }
        assertEquals(comment.getChilds().get(0), comment2);
        assertEquals(comment.getChilds().get(0).getContent(), comment2.getContent());
    }

}
