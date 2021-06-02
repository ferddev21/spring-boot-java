package com.example.demo.student;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentReposity studentReposity;

    @Autowired
    public StudentService(StudentReposity studentReposity) {
        this.studentReposity = studentReposity;
    }

    public List<Student> getStudents() {
        return this.studentReposity.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentByEmail = studentReposity.findStudentByEmail(student.getEmail());

        if (studentByEmail.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        studentReposity.save(student);
    }

    public void deleteStudentById(Long studentId) {
        boolean existsById = studentReposity.existsById(studentId);

        if (!existsById) {
            throw new IllegalStateException("student with id " + studentId + " does not exist");
        }
        studentReposity.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentReposity.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("student with id " + studentId + " dont not exist"));

        if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)) {

            Optional<Student> studentByEmail = studentReposity.findStudentByEmail(email);

            if (studentByEmail.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }
    }
}
