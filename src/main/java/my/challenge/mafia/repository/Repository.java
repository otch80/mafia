package my.challenge.mafia.repository;


import my.challenge.mafia.domain.Member;

import java.util.List;
import java.util.Optional;

public interface Repository {
    Member save(Member member);
    Optional<Member> findById(String id);
    Optional<Member> findOneByIdAndPw(Member member);
    List<Member> findAll();
<<<<<<< HEAD


=======
>>>>>>> 89eb01b1b390974067a5127f012df23423fa4786
}
