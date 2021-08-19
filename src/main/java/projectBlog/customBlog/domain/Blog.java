package projectBlog.customBlog.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Blog {

    @Id @GeneratedValue
    @Column(name="blog_id")
    private int id;
    private String title;
    private String url;

    @OneToOne(mappedBy = "blog", fetch= FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy="blog", fetch=FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();

    private Blog(String title, String url) {
        this.title = title;
        this.url = url; // 디폴트 폴더 존재
    }

    // 연관 관계 메서드 추가
    public static Blog makeBlog(String urlId, Member member) {
        Blog blog = new Blog(urlId + "의 블로그입니다", urlId);
        blog.addMember(member);
        member.addBlog(blog);
        Category.makeParentCategory("default", blog);
        return blog;
    }

    public void changeBlogName(String newTitle) {
        this.title = newTitle;
    }

    public void addMember(Member member) {
        this.member = member;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }
}