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
import com.crud.jason.services.HateoasService;
import javassist.NotFoundException;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alisher Urunov
 */

/*
 * Uncomment @RestController annotation for custom
 * rest points implementation
 */
@RestController
public class EmployeeController {

  private final HateoasService hateoasService;

  EmployeeController(HateoasService hateoasService) {
    this.hateoasService = hateoasService;
  }

  @GetMapping("/employees")
  public ResponseEntity<PagedResources> getAllEmployees(
      Pageable pageable, PagedResourcesAssembler assembler) {
    return new ResponseEntity<>(hateoasService.getAllEmployees(pageable), HttpStatus.OK);
  }

  @GetMapping("/employees/{id}")
  public ResponseEntity<Resource<Employee>> getEmployee(
      @PathVariable Long id, PagedResourcesAssembler assembler) throws NotFoundException {
    return new ResponseEntity<>(hateoasService.getEmployee(id), HttpStatus.OK);
  }

  @DeleteMapping("/employees/{id}")
  public ResponseEntity<Resources<Employee>> deleteEmployee(@PathVariable Long id) {
    hateoasService.deleteEmployee(id);
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  @PostMapping("/employees")
  public ResponseEntity<Resources<Employee>> saveEmployee(@RequestBody @Valid Employee employee)
      throws NotFoundException {
    return new ResponseEntity<>(hateoasService.saveEmployee(employee), HttpStatus.ACCEPTED);
  }

  @PutMapping("/employees/{id}")
  public ResponseEntity<Resources<Employee>> updateEmployee(
      @PathVariable Long id, @RequestBody @Valid Employee employee) throws NotFoundException {
    return new ResponseEntity<>(hateoasService.updateEmployee(id, employee), HttpStatus.ACCEPTED);
  }
}
