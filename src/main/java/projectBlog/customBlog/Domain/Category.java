package projectBlog.customBlog.Domain;

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
public class Category {

    @Id @GeneratedValue
    @Column(name="category_id")
    private int id;
    private String name;

    @Enumerated
    private Status status;

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> childs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="blog_id")
    private Blog blog;

    private Category(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public static Category makeParentCategory(String name) {
        return new Category(name, Status.Parent);
    }

    public static Category makeChildCategory(String name) {
        return new Category(name, Status.Child);
    }

    public void addChildCategory(Category category) throws Exception {
        if(this.status == Status.Parent)  {
            childs.add(category);
            category.setParentCategory(this);
        } else {
            throw new Exception();
        }
    }

    public void setParentCategory(Category category) throws Exception {
        if(this.status == Status.Child) {
            this.parent = category;
        } else {
            throw new Exception();
        }
    }

    public void editContent(String changedName) {
        name = changedName;
    }
}
