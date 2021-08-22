package projectBlog.customBlog.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> childs = new ArrayList<>();

    // category에서 가지고 있는 글들에 접근
    @OneToMany(mappedBy = "category")
    private List<Article> articles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="blog_id")
    private Blog blog;

    private Category(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public static Category makeParentCategory(String name, Blog blog) {
        Category category = new Category(name, Status.Parent);
        category.addBlog(blog);
        blog.addCategory(category);
        return category;
    }

    public static Category makeChildCategory(String name) {
        return new Category(name, Status.Child);
    }

    // 부모 this, 매개변수 자식
    public void addChildCategory(Category category) throws Exception {
        if(this.status == Status.Parent)  {
            this.childs.add(category);
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

    public void deleteCategory(Category category) {
        this.getChilds().remove(category);
    }

    public void editContent(String changedName) {
        name = changedName;
    }

    // 연관관계 메서드
    public void addArticle(Article article) {
        this.articles.add(article);
    }

    public void addBlog(Blog blog) {
        this.blog = blog;
    }
}
