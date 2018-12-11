package cat.tecnocampus.college.persistence;

import domain.College;
import domain.Subject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubjectDAO {
    private JdbcTemplate jdbcTemplate;
    private final String ALL_SUBJECTS = "select code, name, ects, term, level from subject";
    private final String SUBJECT_BY_CODE = "select code, name, ects, term, level from subject where code = ?";

    private RowMapper<Subject> mapper = (resultSet, i) -> {
        Subject subject = new Subject();

        subject.setCode(resultSet.getString("code"));
        subject.setEcts(resultSet.getInt("ects"));
        subject.setName(resultSet.getString("name"));
        subject.setLevel(College.Level.valueOf(resultSet.getString("level")));
        subject.setTerm(College.Terms.valueOf(resultSet.getString("term")));

        return subject;
    };

    public SubjectDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Subject> getAllSubjects() {
        return jdbcTemplate.query(ALL_SUBJECTS, mapper);
    }

    public Subject getSubjectByCode(String code) {
        return jdbcTemplate.queryForObject(SUBJECT_BY_CODE, new Object[]{code}, mapper);
    }
}
