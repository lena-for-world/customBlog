package projectBlog.customBlog.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue
    @Column(name="article_id")
    private int id;
    private String title;
    private String content;
    private LocalDateTime dateTime;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy="article", cascade= CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    private Article(String title, String content, LocalDateTime dateTime) {
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
    }

    // 연관관계 메서드
    public static Article makeArticle(String title, String content, LocalDateTime dateTime, Member member, Category category) {
        Article article = new Article(title, content, dateTime);
        article.addMember(member);
        article.addCategory(category);
        category.addArticle(article);
        return article;
    }

    public String getComment() {
        return comments.get(0).getContent();
    }

    public void editContent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void moveArticleCategory(Category category) {
        this.category = category;
    }

    public void deleteComment(Comment comment) {
        this.getComments().remove(comment);
        //comment.setCommentArticle(null);
    }

    // 연관관계 메서드
    public void addMember(Member member) {
        this.member = member;
    }

    public void addCategory(Category category) {
        this.category = category;
    }

    public void addCommentToArticle(Comment comment) {
        comments.add(comment);
    }
    //
}
