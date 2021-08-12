package projectBlog.customBlog.Domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Article {

    @Id @GeneratedValue
    private int id;
    private String title;
    private String content;
    private LocalDateTime dateTime;

}
