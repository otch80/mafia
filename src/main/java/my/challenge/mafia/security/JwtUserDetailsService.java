package my.challenge.mafia.security;

import my.challenge.mafia.domain.Member;
import my.challenge.mafia.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private Repository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
        전달받은 username을 기반으로 계정을 검색한다.

     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Member member = repository.findById(username).orElseThrow();
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            if (username.equals("admin")) {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }else {
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER")); // 권한 설정
            }

            return new User(member.getId(), member.getPassword(), grantedAuthorities);


        } catch (NoSuchElementException e) {
            return null;
            //return new User(null, null, null);
        }
    }
}
