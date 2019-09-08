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
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.crud.jason.entities.Employee;
import com.crud.jason.repository.EmployeeRepository;

import javassist.NotFoundException;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
/**
 * @author Alisher Urunov
 *
 */

/*	
 * 
 * Uncomment @RestController annotation for custom 
 * rest points implementation
 *
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {

	@Autowired
	EmployeeRepository employeeRepository;
	
	@GetMapping("/employees")
	public Resources<?> getAllEmployees(Pageable pageable) {
		
		Page<Employee> collection = employeeRepository.findAll(pageable);
		collection.forEach(e ->{
			try {
				e.add(linkTo(methodOn(RestController.class).getEmployee(e.getEmployeeId())).withSelfRel());
			} catch (NotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		Link selfLink = linkTo(methodOn(RestController.class).getAllEmployees(pageable)).withSelfRel();
		Resources<Employee> resources = new Resources<>(collection,selfLink);
		
		return resources;
	}
	
	@GetMapping("/employee/{id}")
	public Resource<?> getEmployee(@PathVariable Long id) throws NotFoundException {
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		
		if (!employeeOptional.isPresent()) {
			
		throw new NotFoundException("Can't find employee with ID :" + id);
		}
		Link linkToQueue = linkTo(methodOn(RestController.class).getAllEmployees(PageRequest.of(0, 1))).withSelfRel();
		employeeOptional.get().add(linkTo(methodOn(RestController.class).getEmployee(id)).withSelfRel());
		return new Resource<Employee>(employeeOptional.get(), linkToQueue);
	}
	
	@DeleteMapping("/employee/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		employeeRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/employees")
	public ResponseEntity<?> saveEmployee(@RequestBody @Valid Employee employee) {
		employee.setEmployeeId(0l);
		Employee savedEmployee = employeeRepository.save(employee);
		return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
	}
	
	
	@PutMapping("/employee/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody @Valid Employee employee) throws NotFoundException {
		Optional<Employee> updatedEmployee = employeeRepository.findById(id);
		if(!updatedEmployee.isPresent()) throw new NotFoundException("Can't find employee with ID :" + id);
		employee.setEmployeeId(id);
		employeeRepository.save(employee);
		return new ResponseEntity<>(employee,HttpStatus.OK);
		//Enabling HATEOUS support
//		Resource<Employee> resource = new Resource<Employee>(employee, new Link(""));
//		return resource;
	}
}
