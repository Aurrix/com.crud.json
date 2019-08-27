/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crud.jason.controllers;

import java.lang.module.FindException;
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.crud.jason.entities.Employee;
import com.crud.jason.repository.EmployeeRepository;

import javassist.NotFoundException;

/**
 * @author Alisher Urunov
 *
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {

	@Autowired
	EmployeeRepository employeeRepository;
	
	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		
		return employeeRepository.findAll();
	}
	
	@GetMapping("/employee/{id}")
	public Employee getEmployee(@PathVariable Long id) throws NotFoundException {
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		
		if (!employeeOptional.isPresent()) {
			
		throw new NotFoundException("Can't find employee with ID :" + id);
		}
		
		return employeeOptional.get();
	}
	
	@DeleteMapping("/employee/{id}")
	public void deleteEmployee(@PathVariable Long id) {
		employeeRepository.deleteById(id);
	}
	
	@PostMapping("/employees")
	public ResponseEntity<?> saveEmployee(@RequestBody Employee employee) {
		
		employee.setId(0l);
		
		Employee savedEmployee = employeeRepository.save(employee);
		
		return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
	}
	
	
	@PutMapping("/employee/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) throws NotFoundException {
		Optional<Employee> updatedEmployee = employeeRepository.findById(id);
		if(!updatedEmployee.isPresent()) throw new NotFoundException("Can't find employee with ID :" + id);
		employee.setId(id);
		employeeRepository.save(employee);
		return new ResponseEntity<>(employee,HttpStatus.OK);
	}
}
