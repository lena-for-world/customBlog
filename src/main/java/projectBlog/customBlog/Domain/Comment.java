package projectBlog.customBlog.Domain;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Comment {

    @Id @GeneratedValue
    private int id;
    private String content;

    @Enumerated
    private Status status;

}
