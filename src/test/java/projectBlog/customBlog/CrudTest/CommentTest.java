package projectBlog.customBlog.CrudTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import projectBlog.customBlog.domain.Article;
import projectBlog.customBlog.domain.Blog;
import projectBlog.customBlog.domain.Category;
import projectBlog.customBlog.domain.Comment;
import projectBlog.customBlog.domain.Member;
import projectBlog.customBlog.domain.Status;

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
    Blog blog;

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
        blog = new Blog();
        category = Category.makeParentCategory("category", blog);
        article = Article.makeArticle(title, content, LocalDateTime.now(), member, category);
        save(blog);
        save(category);
        save(member);
        save(article);

        comment = Comment.makeParentComment("게시물 1의 댓글!!", article); // article에 댓글 생성
//        article.addCommentToArticle(comment);
//        comment.setCommentArticle(article);
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

        Comment comment2 = Comment.makeParentComment("게시물 2의 댓글!!2222", article2);
 //       article2.addCommentToArticle(comment2);
 //       comment2.setCommentArticle(article2);
        save(comment2);

        Comment comment3 = Comment.makeParentComment("게시물 1의 댓글!!3333", article);
 //       article.addCommentToArticle(comment3);
 //       comment3.setCommentArticle(article);
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
        Comment comment2 = Comment.makeParentComment("부모댓글!!2222", article);
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

    @Test
    @DisplayName("글 삭제 시 댓글 삭제")
    public void whenDeleteArticleThenComment() {
        List<Comment> comments = em.createQuery("select cm from Comment cm", Comment.class).getResultList();
        assertEquals(comments.size(), 1);
        delete(article);
        comments = em.createQuery("select cm from Comment cm", Comment.class).getResultList();
        assertEquals(comments.size(), 0);
    }

    @Test
    @DisplayName("댓글 삭제 시 댓글만 삭제, 글 삭제 ㄴㄴ")
    public void whenDeleteCommentThenArticle() {

        // given
        List<Comment> comments = em.createQuery("select cm from Comment cm", Comment.class).getResultList();
        assertEquals(comments.size(), 1);
        List<Article> articles = em.createQuery("select a from Article a", Article.class).getResultList();
        assertEquals(articles.size(), 1);
        assertEquals(articles.get(0).getComments().size(), 1);

        // when
        //deleteComment(comment);
        article.deleteComment(comment);
        delete(comment);

        // then
        comments = em.createQuery("select cm from Comment cm", Comment.class).getResultList();
        assertEquals(comments.size(), 0);
        articles = em.createQuery("select a from Article a", Article.class).getResultList();
        assertEquals(articles.size(), 1);
        assertEquals(articles.get(0).getComments().size(), 0);

    }

    @Test
    @DisplayName("부모 댓글 삭제")
    public void deleteParentComment() {

        // given
        assertEquals(Status.Parent, comment.getStatus());
        assertEquals(article.getComments().size(), 1);

        // when
        comment.getArticle().deleteComment(comment);
        delete(comment);

        // then
        assertEquals(article.getComments().size(), 0);
    }

    @Test
    @DisplayName("자식 댓글 삭제")
    public void deleteChildComment() {

        // given
        assertEquals(comment.getChilds().size(), 0);

        // when
        Comment child = Comment.makeChildComment("자식 댓글");
        try {
            comment.addChildComment(child);
        } catch(Exception e) {
            e.printStackTrace();
        }
        assertEquals(Status.Child, child.getStatus());
        assertEquals(article.getComments().size(), 1);
        assertEquals(comment.getChilds().size(), 1);

        // then
        child.getParent().removeConnectionFromParent(child);
        delete(child);
        assertEquals(comment.getChilds().size(), 0);
    }

    @Test
    @DisplayName("실제 서비스 - 대댓글 달기")
    public void makechildComment() {
        Comment child = Comment.makeChildComment("대댓글 내용");
        try {
            comment.addChildComment(child);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("댓글 수정")
    public void editComment() {

        //given
        Comment pComment = em.find(Comment.class, comment.getId());
        String newContent ="바뀐 내용";
        assertNotEquals(pComment.getContent(), newContent);

        //when
        pComment.editContent(newContent);

        //then
        assertEquals(pComment.getContent(), newContent);
    }

}
