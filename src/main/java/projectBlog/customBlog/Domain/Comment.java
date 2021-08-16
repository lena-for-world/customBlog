package projectBlog.customBlog.Domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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
public class Comment {

    @Id @GeneratedValue
    @Column(name="comment_id")
    private int id;
    private String content;

    @Enumerated
    private Status status;

    @ManyToOne
    @JoinColumn(name="article_id")
    private Article article;

    @OneToMany(mappedBy = "parent")
    private List<Comment> childs;

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Comment parent;

    private Comment(String content, Status status) {
        this.content = content;
        this.status = status;
    }

    public static Comment makeParentComment(String content) {
        return new Comment(content, Status.Parent);
    }

    public static Comment makeChildComment(String content) {
        return new Comment(content, Status.Child);
    }

    public void setCommentArticle(Article article) {
        this.article = article;
    }

}