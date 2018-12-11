package domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Student {
    @NotNull(message = "username cannot be null")
    @Size(min = 4, max = 15, message = "username must be between 4 an 15 characters long")
    @Pattern(regexp = "^\\w+", message = "must have alphanumeric characters")
    private String name;

    @NotNull(message = "Second name cannot be null")
    @Size(min = 4, max = 50, message = "Second name must be between 4 an 15 characters long")
    @Pattern(regexp = "^[A-Z].+", message = "Second name must match \\^[A-Z].*")
    private String secondName;

    @NotNull(message = "email cannot be null")
    @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\\b",
            message = "Email must match \\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\\b")
    private String email;

    @NotNull(message = "password cannot be null")
    @Size(min = 4, max = 50, message = "Password must be between 4 an 15 characters long")
    private String password;

    private List<Registration> registrations;

    public Student() {
        registrations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistration(Registration registration) {
        registrations.add(registration);
    }

    public boolean passedSubject(Subject subject) {
        return registrations.stream().anyMatch(r -> r.passedSubject(subject));
    }

    public List<Subject> getPassedSubjects() {
        return registrations.stream().flatMap(r -> r.getPassedSubjects().stream()).collect(Collectors.toList());
    }

    public List<Subject> getNonEvaluatedSubjects() {
        return registrations.stream().flatMap(r -> r.getNonEvaluatedSubjects().stream()).collect(Collectors.toList());
    }
    public Set<Subject> getAllRegisteredSubjects() {
        return registrations.stream().flatMap(r -> r.getSubjects().stream()).collect(Collectors.toSet());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        return email.equals(student.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registrations='" + registrations.size() + '\'' +
                '}';
    }
}
