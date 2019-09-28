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

import com.crud.jason.entities.Employee;
import com.crud.jason.repository.EmployeeRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
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
class EmployeeController {
	
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@GetMapping("/employees")
	public ResponseEntity<?> getAllEmployees(Pageable pageable,PagedResourcesAssembler assembler) {

		Page<Employee> collection = employeeRepository.findAll(pageable);
		collection.forEach(e ->{
			try {
				e.add(linkTo(methodOn(EmployeeController.class).getEmployee(e.getEmployeeId(), assembler)).withSelfRel());
			} catch (NotFoundException e1) {
				e1.printStackTrace();
			}
		});
		
		PageMetadata pageMetaData = new PagedResources.PageMetadata(pageable.getPageSize(), pageable.getPageNumber(), collection.getSize());
		
		Resources<Employee> resources =  assembler.toResource(collection);
		
		return new ResponseEntity<>(resources,HttpStatus.OK);
	}

	@GetMapping("/employees/{id}")
	public Resource<Employee> getEmployee(@PathVariable Long id, PagedResourcesAssembler assembler) throws NotFoundException {
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		
		if (!employeeOptional.isPresent()) {
			
		throw new NotFoundException("Can't find employee with ID :" + id);
		}
		Link linkToQueue = linkTo(methodOn(EmployeeController.class).getAllEmployees(PageRequest.of(0, 1), assembler)).withSelfRel();
		employeeOptional.get().add(linkTo(methodOn(EmployeeController.class).getEmployee(id, assembler)).withSelfRel());
		return new Resource<Employee>(employeeOptional.get(), linkToQueue);
	}

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		employeeRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/employees")
	public Resources<Employee> saveEmployee(@RequestBody @Valid Employee employee,PagedResourcesAssembler assembler) throws NotFoundException {
		
		Employee savedEmployee = employeeRepository.save(employee);
		savedEmployee.add(linkTo(methodOn(EmployeeController.class).getEmployee(employee.getEmployeeId(), assembler)).withSelfRel());
		
		return new Resources<Employee>(
			new ArrayList<>(Arrays.asList(savedEmployee)),
				linkTo(methodOn(EmployeeController.class).getAllEmployees(PageRequest.of(0, 1), assembler)).withSelfRel());
	}


	@PutMapping("/employees/{id}")
	public Resources<Employee> updateEmployee(@PathVariable Long id, @RequestBody @Valid Employee employee,PagedResourcesAssembler assembler) throws NotFoundException {
		Optional<Employee> updatedEmployee = employeeRepository.findById(id);
		if(!updatedEmployee.isPresent()) throw new NotFoundException("Can't find employee with ID :" + id);
		employee.setEmployeeId(id);
		employeeRepository.save(employee);
		employee.add(linkTo(methodOn(EmployeeController.class).getEmployee(employee.getEmployeeId(), assembler)).withSelfRel());
		return new Resources<Employee>(
				new ArrayList<>(Arrays.asList(employee)),
				linkTo(methodOn(EmployeeController.class).getAllEmployees(PageRequest.of(0, 1), assembler)).withSelfRel());

	}
}
