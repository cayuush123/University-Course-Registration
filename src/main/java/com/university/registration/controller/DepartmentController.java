package com.university.registration.controller;



import com.university.registration.entity.Department;
import com.university.registration.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/departments")
    @CrossOrigin(origins = "*")
    public class DepartmentController {

        @Autowired
        private DepartmentService service;

        @GetMapping
        public List<Department> getAllDepartments() {
            return service.getAllDepartments();
        }

        @GetMapping("/{id}")
        public Department getDepartment(@PathVariable Long id) {
            return service.getDepartmentById(id);
        }

        @PostMapping
        public Department createDepartment(@RequestBody Department department) {
            return service.saveDepartment(department);
        }

        @PutMapping("/{id}")
        public Department updateDepartment(@PathVariable Long id,
                                           @RequestBody Department department) {
            return service.updateDepartment(id, department);
        }

        @DeleteMapping("/{id}")
        public void deleteDepartment(@PathVariable Long id) {
            service.deleteDepartment(id);
        }
    }


