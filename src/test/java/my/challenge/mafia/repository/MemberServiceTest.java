package my.challenge.mafia.repository;

import my.challenge.mafia.domain.Member;
import my.challenge.mafia.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void signup(){
        //given
        Member member = new Member();
        member.setId("spring");
        member.setPassword("spring");

        //when
        String id = memberService.join(member);

        //then
        Member findMember = memberService.findById(id).get();
        assertThat(member.getId()).isEqualTo(findMember.getId());
    }

}
