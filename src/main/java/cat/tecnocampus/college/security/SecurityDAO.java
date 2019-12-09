package cat.tecnocampus.college.security;

import domain.Lecturer;
import domain.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityDAO {
    JdbcTemplate jdbcTemplate;

    private final String INSERT_ROLE = "INSERT INTO authorities (email, role) values(?, ?)";

    public SecurityDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createUserRole(Student student) {
        jdbcTemplate.update(INSERT_ROLE, student.getEmail(), "ROLE_USER");
    }
    public void createUserRole(Lecturer lecturer) {
        jdbcTemplate.update(INSERT_ROLE, lecturer.getEmail(), "ROLE_USER");
    }
}
