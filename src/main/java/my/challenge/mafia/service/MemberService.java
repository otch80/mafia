package my.challenge.mafia.service;

import my.challenge.mafia.domain.Member;
import my.challenge.mafia.repository.Repository;

import java.util.List;
import java.util.Optional;

public class MemberService {
    private final Repository repository;

    public MemberService(Repository repository){
        this.repository = repository;
    }

    public String join(Member member){
        repository.save(member);
        return member.getId();
    }

    public List<Member> findAllMember(){
        return repository.findAll();
    }

    public Optional<Member> findById(String id){
        return repository.findById(id);
    }

    public Optional<Member> idPwCheck(Member member){
        return repository.findOneByIdAndPw(member);
    }


}
