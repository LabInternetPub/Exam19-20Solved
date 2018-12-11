package cat.tecnocampus.college.persistence;

import domain.Registration;
import domain.Student;
import domain.exceptions.StudentDoesNotExistsException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDAO {
    JdbcTemplate jdbcTemplate;
    RegistrationDAO registrationDAO;

    private final String QUERY_ALL_STUDENTS_LAZY = "select name, second_name, email, password from student";
    private final String QUERY_STUDENT_LAZY = "select name, second_name, email, password from student where email = ?";
    private final String INSERT_STUDENT = "INSERT INTO student (name, second_name, email, password) VALUES (?, ?, ?, ?)";

    private RowMapper<Student> mapper = (resultSet, i) -> {
        Student student = new Student();
        student.setEmail(resultSet.getString("email"));
        student.setName(resultSet.getString("name"));
        student.setPassword(resultSet.getString("password"));
        student.setSecondName(resultSet.getString("second_name"));

        return student;
    };

    public StudentDAO(JdbcTemplate jdbcTemplate, RegistrationDAO registrationDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.registrationDAO = registrationDAO;
    }

    public List<Student> queryAllStudentsLazy() {
        return jdbcTemplate.query(QUERY_ALL_STUDENTS_LAZY, mapper);
    }

    public Student queryStudentLazy(String email) {
        try {
            return jdbcTemplate.queryForObject(QUERY_STUDENT_LAZY, new Object[]{email}, mapper);
        } catch (EmptyResultDataAccessException e) {
            throw new StudentDoesNotExistsException(email);
        }
    }

    public Student getStudentEager(String email) {
        List<Registration> registrations = registrationDAO.queryRegistrationsFromUser(email);
        if (registrations.isEmpty())
            return queryStudentLazy(email);
        return registrations.get(0).getStudent();
    }

    public void saveStudent(Student student) {
        jdbcTemplate.update(INSERT_STUDENT, student.getName(), student.getSecondName(), student.getEmail(), student.getPassword());
    }

}
