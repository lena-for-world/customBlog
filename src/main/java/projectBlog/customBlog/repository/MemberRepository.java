package projectBlog.customBlog.repository;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import projectBlog.customBlog.domain.Category;
import projectBlog.customBlog.domain.Member;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Member findMember(int memberId) {
        return em.find(Member.class, memberId);
    }
}
