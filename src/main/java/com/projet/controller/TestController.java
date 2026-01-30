package com.projet.controller;

import com.framework.annotation.RestController;
import com.framework.annotation.GetMapping;
import com.framework.annotation.PathVariable;
import com.framework.annotation.CrossOrigin;
import com.framework.util.ResponseEntity;
import com.framework.util.HttpStatus;

import com.projet.model.Test;
import com.projet.repository.TestRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class TestController {

    private TestRepository testRepository = new TestRepository();

    @GetMapping("/api/tests")
    public ResponseEntity<List<Test>> getAllTests() {
        try {
            List<Test> tests = testRepository.findAll();
            return ResponseEntity.ok(tests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/api/tests/{id}")
    public ResponseEntity<Test> getTestById(@PathVariable("id") int id) {
        try {
            Test test = testRepository.findById(id);
            if (test != null) {
                return ResponseEntity.ok(test);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
