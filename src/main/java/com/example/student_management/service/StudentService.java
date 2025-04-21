package com.example.student_management.service;

import com.example.student_management.entity.Department;
import com.example.student_management.entity.Student;
import com.example.student_management.repository.StudentRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Add a single student
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    // Delete a student by ID
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> processCSV(MultipartFile file) throws IOException {
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            try (CSVParser csvParser = CSVParser.parse(reader, CSVFormat.DEFAULT
                    .withHeader("name", "email", "phone", "department") // Define the header names
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim())) {

                List<Student> students = csvParser.getRecords().stream()
                        .map(this::createStudentFromRecord) // Use method reference
                        .collect(Collectors.toList());
                return studentRepository.saveAll(students); //save all students
            }
        }
    }

    private Student createStudentFromRecord(CSVRecord record) {
        Student student = new Student();
        student.setName(record.get("name"));
        student.setEmail(record.get("email"));
        student.setPhone(record.get("phone"));
        // Handle potential errors in department parsing
        try {
            student.setDepartment(Department.valueOf(record.get("department").toUpperCase())); //make it upper case
        } catch (IllegalArgumentException e) {
            // Log the error or handle it appropriately (e.g., set a default, skip the student)
            System.err.println("Error parsing department: " + record.get("department") + ".  Setting department to null.");
            student.setDepartment(null); // Or set a default value
        }
        return student;
    }

    public List<Student> processExcel(MultipartFile file) throws IOException {
        // Implementation for Excel processing
        //  You would typically use a library like Apache POI to read the Excel file
        throw new UnsupportedOperationException("Excel processing not implemented yet");
    }
}
