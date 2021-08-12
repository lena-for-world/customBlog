package projectBlog.customBlog.domainChange;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import projectBlog.customBlog.Domain.Category;

@Entity
public class Article {

    @Id @GeneratedValue
    @Column(name="article_id")
    private int id;
    private String title;
    private String content;
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

}
