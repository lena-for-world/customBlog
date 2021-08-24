package projectBlog.customBlog.repository;

import java.util.List;
import java.util.Optional;
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

    public List<Member> findAll() {
        return em.createQuery("select m from Member m").getResultList();
    }

    public Optional<Member> findByUserId(String userId) {
        return findAll().stream()
            .filter(m->m.getUserId().equals(userId))
            .findFirst();
    }
}
