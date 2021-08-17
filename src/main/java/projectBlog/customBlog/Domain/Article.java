package projectBlog.customBlog.Domain;

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

@Entity
@Getter
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

    private Article(String title, String content, LocalDateTime dateTime, Member member, Category category) {
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.member = member;
        this.category = category;
    }

    public static Article makeArticle(String title, String content, LocalDateTime dateTime, Member member, Category category) {
        return new Article(title, content, dateTime, member, category);
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
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

    public void addCommentToArticle(Comment comment) {
        comments.add(comment);
    }
}
