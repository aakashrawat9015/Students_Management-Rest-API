package com.example.student_management.controller;

import com.example.student_management.entity.ApiResponse;
import com.example.student_management.entity.Student;
import com.example.student_management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Import MultipartFile
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // Add a single student
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student savedStudent = studentService.addStudent(student);
        return ResponseEntity.ok(savedStudent);
    }

    // Delete a student by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // Get all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }


    // Upload students from CSV file
    @PostMapping("/upload/csv")
    public ResponseEntity<ApiResponse<List<Student>>> uploadCSV(@RequestParam("file") MultipartFile file) { // Corrected ApiResponse
        try {
            List<Student> students = studentService.processCSV(file);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Uploaded successfully", students)
            );
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Error processing file: " + e.getMessage(), null)  // Corrected ApiResponse
            );
        }
    }
}

