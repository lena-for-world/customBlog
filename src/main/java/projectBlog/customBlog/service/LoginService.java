package projectBlog.customBlog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projectBlog.customBlog.domain.Member;
import projectBlog.customBlog.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String userId, String password) {
        return memberRepository.findByUserId(userId)
            .filter(m->m.getPassword().equals(password))
            .orElse(null);
    }

}
