package projectBlog.customBlog.domainChange;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Category {

    @Id @GeneratedValue
    private int id;
    private String name;

    @Enumerated
    private Status status;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> childs;
}
