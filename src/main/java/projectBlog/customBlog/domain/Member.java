package projectBlog.customBlog.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private int id;
    private String userId;
    private String name;

    @OneToOne(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="blog_id")
    private Blog blog;

    private Member(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public static Member makeMember(String userId, String name) {
       Member member = new Member(userId, name);
       Blog.makeBlog(userId, member);
       return member;
    }

    // 연관관계 메서드
    public void addBlog(Blog blog) {
        this.blog = blog;
    }

}