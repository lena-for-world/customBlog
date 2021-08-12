package projectBlog.customBlog.domainChange;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Comment {

    @Id @GeneratedValue
    private int id;
    private String content;

    @Enumerated
    private Status status;

    @ManyToOne
    @JoinColumn(name="article_id")
    private Article article;
}
