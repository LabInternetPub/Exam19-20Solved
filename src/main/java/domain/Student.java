package domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Student extends User {

    private List<Registration> registrations;

    public Student() {
        registrations = new ArrayList<>();
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
