package cat.tecnocampus.college.persistence;

import domain.College;
import domain.Registration;
import domain.Student;
import domain.Subject;
import domain.exceptions.StudentWithNoRegistrations;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class RegistrationDAO {
    private JdbcTemplate jdbcTemplate;

    private final String QUERY_REGISTRATIONS = "SELECT st.email as student_email, st.name as student_name, st.second_name as student_second_name,  st.password as student_password, " +
            "r.date as date, r.academic_year as academic_year, " +
            "sb.name as subjects_name, sb.code as subjects_code, sb.ects as subjects_ects, sb.term as subjects_term, sb.level as subjects_level,  " +
            "c.subject as grade_key, c.mark as grade_mark from registration r " +
            "inner join convocation c on r.date = c.date and c.student = r.student " +
            "inner join student st on r.student = st.email " +
            "inner join subject sb on c.subject = sb.code " +
            "order by (r.student, r.date)";

    private final String QUERY_REGISTRATIONS_FROM_USER =
            "SELECT st.email as student_email, st.name as student_name, st.second_name as student_second_name,  st.password as student_password, " +
            "r.date as date, r.academic_year as academic_year, " +
            "sb.name as subjects_name, sb.code as subjects_code, sb.ects as subjects_ects, sb.term as subjects_term, sb.level as subjects_level,  " +
            "c.mark as grade_mark " +
            "from registration r " +
            "inner join convocation c on r.date = c.date and c.student = r.student " +
            "inner join student st on r.student = st.email " +
            "inner join subject sb on c.subject = sb.code " +
            "where r.student = ? " +
            "order by (r.student, r.date)";

    private final String INSERT_REGISTRATION = "INSERT INTO registration (student, academic_year, date) VALUES (?, ? , ?)";
    private final String INSERT_CONVOCATION = "INSERT INTO convocation (date, student, subject) VALUES (?, ? , ?)";


    private ResultSetExtractor<List<Registration>> rse = rs -> {
        ArrayList<Registration> registrations = new ArrayList<>();
        Registration currentRegistration = null;
        while (rs.next()) {
            String studentEmail = rs.getString("student_email");
            LocalDate date = rs.getDate("date").toLocalDate();

            if (currentRegistration == null || !currentRegistration.getStudent().getEmail().equals(studentEmail) || !currentRegistration.getDate().equals(date)) {
                currentRegistration = new Registration();
                currentRegistration.setDate(date);
                currentRegistration.setAcademicYear(rs.getString("academic_year"));

                Student student = new Student();
                student.setEmail(studentEmail);
                student.setName(rs.getString("student_name"));
                student.setSecondName(rs.getString("student_second_name"));
                student.setPassword(rs.getString("student_password"));
                currentRegistration.setStudent(student);

                registrations.add(currentRegistration);
            }

            Subject subject = new Subject();
            subject.setName(rs.getString("subjects_name"));
            subject.setTerm(College.Terms.valueOf(rs.getString("subjects_term")));
            subject.setLevel(College.Level.valueOf(rs.getString("subjects_level")));
            subject.setEcts(rs.getInt("subjects_ects"));
            subject.setCode(rs.getString("subjects_code"));
            currentRegistration.registerSubject(subject);

            currentRegistration.grade(subject, rs.getDouble("grade_mark"));
        }

        //registrations may represent the same student with different objects. Must avoid this
        sameStudentSameObject(registrations);

        //For the double association direction: set registrations to student
        registrations.forEach(r -> r.getStudent().setRegistration(r));

        return registrations;
    };

    public RegistrationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Registration> queryRegistrations() {
        return jdbcTemplate.query(QUERY_REGISTRATIONS, rse);
    }

    public List<Registration> queryRegistrationsFromUser(String email) {
        return jdbcTemplate.query(QUERY_REGISTRATIONS_FROM_USER, new Object[]{email}, rse);
    }

    @Transactional
    public void registerSubjects(Registration registration) {
        saveRegistration(registration);
        saveConvocations(registration);
    }

    private void saveRegistration(Registration registration) {
        jdbcTemplate.update(INSERT_REGISTRATION, registration.getStudent().getEmail(), registration.getAcademicYear(), registration.getDate());
    }

    private void saveConvocations(Registration registration) {
        jdbcTemplate.batchUpdate(INSERT_CONVOCATION, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Subject subject = registration.getSubjects().get(i);
                preparedStatement.setDate(1, Date.valueOf(registration.getDate()));
                preparedStatement.setString(2, registration.getStudent().getEmail());
                preparedStatement.setString(3, subject.getCode());
            }

            @Override
            public int getBatchSize() {
                return registration.getSubjects().size();
            }
        });

    }

    public Registration queryLastRegistrationFromUser(String email) {
        List<Registration> registrations = queryRegistrationsFromUser(email);

        if (registrations.isEmpty())
            throw new StudentWithNoRegistrations(email);

        return registrations.get(registrations.size() - 1);
    }


    private void sameStudentSameObject(List<Registration> registrations) {
        HashMap<String, Student> students = new HashMap<>();
        registrations.forEach(r -> {students.putIfAbsent(r.getStudent().getEmail(),r.getStudent()); r.setStudent(students.get(r.getStudent().getEmail()));});
    }
}
