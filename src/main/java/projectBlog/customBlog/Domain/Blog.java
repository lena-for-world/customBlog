package projectBlog.customBlog.Domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Blog {

    @Id @GeneratedValue
    private int id;
    private String title;

    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "blog_id")
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "blog_id")
    private List<Article> articles = new ArrayList<>();
}