package projectBlog.customBlog.domainChange;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private int id;
    private String userId;
    private String name;

}