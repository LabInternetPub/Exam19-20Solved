package cat.tecnocampus.college.persistence;

import domain.Lecturer;
import domain.Subject;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class LecturerDAO {
    private static int LAST_DECEMBER = 365;
    private static int FIRST_AUGUST = 213;
    private final JdbcTemplate jdbcTemplate;
    private final ResultSetExtractorImpl<Lecturer> rse;

    private final String QUERY_ALL_LECTURERS_CURRENT_SUBJECTS = "select u.name, u.second_name, u.email, u.password, " +
            "s.code as currentSubjects_code, s.name as currentSubjects_name, s.term as currentSubjects_term, " +
            "s.level as currentSubjects_level, s.ects as currentSubjects_ects " +
            "from myuser u " +
            "inner join authorities a on u.email = a.email " +
            "left outer join impartition i on i.lecturer = u.email " +
            "inner join subject s on s.code = i.subject " +
            "where a.role like '%LECTURER'";

    private final String QUERY_LECTURER_LAZY = "select name, second_name, email, password from myuser where email = ?";
    private final String INSERT_LECTURER = "INSERT INTO myuser (name, second_name, email, password) VALUES (?, ?, ?, ?)";
    private final String INSERT_LECTURER_ROLE = "INSERT INTO authorities (email, role) values(?,'ROLE_LECTURER')";
    private final String INSERT_IMPARTITION = "INSERT INTO impartition (lecturer, subject, academic_year) VALUES (?, ?, ?)";

    public LecturerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rse = JdbcTemplateMapperFactory
                .newInstance()
                .addKeys("email", "code")
                .newResultSetExtractor(Lecturer.class);

    }

    public List<Lecturer> getLecturersWithCurrentSubjects() {
        List<Lecturer> lecturers = this.jdbcTemplate
                .query(QUERY_ALL_LECTURERS_CURRENT_SUBJECTS, rse);
        return lecturers;
    }

    public void saveLecturer(Lecturer lecturer) {
        jdbcTemplate.update(INSERT_LECTURER, lecturer.getName(), lecturer.getSecondName(), lecturer.getEmail(), lecturer.getPassword());
        jdbcTemplate.update(INSERT_LECTURER_ROLE, lecturer.getEmail());
        jdbcTemplate.batchUpdate(INSERT_IMPARTITION, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Subject subject = lecturer.getCurrentSubjects().get(i);
                preparedStatement.setString(1, lecturer.getEmail());
                preparedStatement.setString(2, subject.getCode());
                preparedStatement.setString(3, calculateAcademicYear());
            }

            @Override
            public int getBatchSize() {
                return lecturer.getCurrentSubjects().size();
            }
        });
    }

    private String calculateAcademicYear() {
        LocalDate date = LocalDate.now();

        if (date.getDayOfYear() >= FIRST_AUGUST && date.getDayOfYear() <= LAST_DECEMBER) {
            return String.valueOf(date.getYear()) + "/" + String.valueOf(date.getYear() + 1);
        } else {
            return String.valueOf(date.getYear() - 1) + "/" + String.valueOf(date.getYear());

        }
    }
}
