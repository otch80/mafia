package my.challenge.mafia.repository;

import my.challenge.mafia.domain.Member;
import my.challenge.mafia.repository.exception.DuplicateInsertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MemberRepository implements Repository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MemberRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public Member save(Member member) {
        // update 메소드는 리턴값이 없다. insert, update, delete 쿼리문을 날릴 때 사용한다.

        try {
            jdbcTemplate.update("insert into members values(?,?)", member.getId(), member.getPassword());
        } catch (DataAccessException e) {
            throw new DuplicateInsertException();
        }

        return member;
    }

    @Override
    public Optional<Member> findById(String id) {
        List<Member> result = jdbcTemplate.query("select * from members where id = ?", memberRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findOneByIdAndPw(Member member) {
        // RowMapper 객체를 전달하여 쿼리문의 결과를 그곳에 담고 List형태로 반환한다.
        // 순수 JDBC에서는 resultSet을 통해 받는다.
        // 코드를 간소화 시켜주는 JdbcTemplate의 장점
        List<Member> result = jdbcTemplate.query("select * from members where id = ? && password = ?", memberRowMapper(), member.getId(), member.getPassword());
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    // 쿼리문의 결과를 담기 위한 객체
    private RowMapper<Member> memberRowMapper() {
        return new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Member member = new Member();
                member.setId(rs.getString("id"));
                member.setPassword(rs.getString("password"));
                return member;
            }
        };

//        return (rs, rowNum) -> {
//            Member member = new Member();
//            member.setId(rs.getString("id"));
//            member.setPassword(rs.getString("password"));
//            return member;
//        };
    }
}
