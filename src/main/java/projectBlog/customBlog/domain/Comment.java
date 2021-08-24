package projectBlog.customBlog.domain;

import java.util.ArrayList;
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
    private List<Comment> childs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Comment parent;

    private Comment(String content, Status status) {
        this.content = content;
        this.status = status;
    }

    public static Comment makeParentComment(String content, Article article) {
        Comment comment = new Comment(content, Status.Parent);
        comment.setCommentArticle(article);
        article.addCommentToArticle(comment);
        return comment;
    }

    public static Comment makeChildComment(String content) {
        return new Comment(content, Status.Child);
    }

    public void editContent(String changedContent) {
        content = changedContent;
    }

    public void addChildComment(Comment comment) throws Exception {
        if(this.status == Status.Parent)  {
            childs.add(comment);
            comment.setParentComment(this);
        } else {
            throw new Exception();
        }
    }

    public void setParentComment(Comment comment) throws Exception {
        if(this.status == Status.Child) {
            this.parent = comment;
        } else {
            throw new Exception();
        }
    }

    public void setCommentArticle(Article article) {
        this.article = article;
    }

    public void removeConnectionFromParent(Comment comment) {
        this.getChilds().remove(comment);
    }


}