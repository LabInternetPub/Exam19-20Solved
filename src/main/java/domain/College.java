package domain;

import domain.exceptions.RegisterPreviousLevelSubjectsFirst;

import java.util.*;
import java.util.stream.Collectors;

public class College {
    public enum Terms {
        T1, T2, T3
    }

    public enum Level {
        L1, L2, L3, L4;
    }

    private Map<Level, List<Subject>> subjectsInLevels;
    private Map<String, Subject> subjectMap;

    public College() {
        subjectsInLevels = new HashMap<>();
        subjectMap = new HashMap<>();
    }

    public void setSubjects(List<Subject> subjects) {
        subjects.forEach(s ->
            {if (this.subjectsInLevels.containsKey(s.getLevel())) {
                this.subjectsInLevels.get(s.getLevel()).add(s);
            }
            else {
                List<Subject> levelSubject = new ArrayList<>();
                levelSubject.add(s);
                this.subjectsInLevels.put(s.getLevel(), levelSubject);
            }

            }
        );

        subjects.forEach(s -> subjectMap.put(s.getCode(), s));
    }

    public List<Subject> getSubjectsInLevel(Level level) {
        return subjectsInLevels.get(level);
    }

    public List<Subject> getSubjects() {
        return subjectsInLevels.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public Subject getSubject(String code) {
        return subjectMap.get(code);
    }

    public Registration registerSubjects(Student student, List<Subject> subjects) {
        if (!registeredAllPreviousSubjects(student,subjects))
            throw new RegisterPreviousLevelSubjectsFirst("Sudent: " + student.getEmail() + " must setSubject subjects in previous levels");

        Registration registration = new Registration();
        registration.setStudent(student);
        student.setRegistration(registration);

        subjects.forEach(s->registration.registerSubject(s));

        return registration;
    }

    public Map<Level, List<Subject>> getSubjectsStillToRegister(Student student) {
        Map<Level, List<Subject>> toDoSubjects = copy(subjectsInLevels);

        //remove passed subjects
        student.getPassedSubjects().forEach(s -> toDoSubjects.get(s.getLevel()).remove(s));

        //remove non evaluated subjects
        student.getNonEvaluatedSubjects().forEach(s -> toDoSubjects.get(s.getLevel()).remove(s));

        return toDoSubjects;
    }

    private static Map<Level, List<Subject>> copy(Map<Level, List<Subject>> original)
    {
        Map<Level, List<Subject>> copy = new HashMap<>();
        for (Map.Entry<Level, List<Subject>> entry : original.entrySet())
        {
            copy.put(entry.getKey(), new ArrayList<Subject>(entry.getValue()));
        }
        return copy;
    }

    private boolean registeredAllPreviousSubjects(Student student, List<Subject> subjects) {
        Set<Level> registeredLevels = new HashSet<>();
        subjects.forEach(s -> registeredLevels.add(s.getLevel()));
        // Find highest level
        Level max = registeredLevels.stream().max(Comparator.comparing(Level::ordinal)).orElseThrow(NoSuchElementException::new);

        Map<Level, List<Subject>> toDoSubjects = copy(subjectsInLevels);

        //remove registering subjects
        subjects.forEach(s -> toDoSubjects.get(s.getLevel()).remove(s));

        //remove passed subjects
        student.getPassedSubjects().forEach(s -> toDoSubjects.get(s.getLevel()).remove(s));

        //remove non evaluated subjects
        student.getNonEvaluatedSubjects().forEach(s -> toDoSubjects.get(s.getLevel()).remove(s));

        //Highest level and above can have remaining subjects: don't check whether is empty
        EnumSet.allOf(Level.class)
                .forEach(l -> {if (l.ordinal() >= max.ordinal()) toDoSubjects.remove(l);});

        return toDoSubjects.values().stream().allMatch(List::isEmpty);
    }

}
