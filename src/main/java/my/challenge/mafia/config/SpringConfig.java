package my.challenge.mafia.config;


import my.challenge.mafia.repository.MemberRepository;
import my.challenge.mafia.repository.Repository;
import my.challenge.mafia.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private DataSource dataSource;

    @Autowired
    public SpringConfig(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(repository());
    }

    @Bean
    public Repository repository(){
        return new MemberRepository(dataSource);
    }

}
// change ================= dundole
