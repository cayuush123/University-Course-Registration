package com.university.registration.service;


import com.university.registration.entity.Department;
import com.university.registration.repository.DepartmentRepository;
import com.university.registration.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
    public class DepartmentServiceImpl implements DepartmentService {
        @Autowired
        private DepartmentRepository repository;
        @Override
        public List<Department> getAllDepartments() {
            return repository.findAll();
        }
        @Override
        public Department getDepartmentById(Long id) {
            return repository.findById(id)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Department not found"));    }
        @Override
        public Department saveDepartment(Department department) {
            return repository.save(department);
        }
        @Override
        public Department updateDepartment(Long id, Department department) {
            Department oldDepartment = repository.findById(id).orElse(null);
            if (oldDepartment != null) {
                oldDepartment.setDepartmentName(department.getDepartmentName());
                oldDepartment.setFaculty(department.getFaculty());
                return repository.save(oldDepartment);
            }
            return null;
        }
        @Override
        public void deleteDepartment(Long id) {
            repository.deleteById(id);
        }
    }


