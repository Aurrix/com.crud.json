package com.crud.jason.services;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.crud.jason.controllers.EmployeeController;
import com.crud.jason.entities.Employee;
import com.crud.jason.repository.EmployeeRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Service;

@Service
public class HateoasService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final EmployeeRepository employeeRepository;
  private final PagedResourcesAssembler assembler;

  public HateoasService(EmployeeRepository employeeRepository, PagedResourcesAssembler assembler) {
    this.employeeRepository = employeeRepository;
    this.assembler = assembler;
  }

  public PagedResources getAllEmployees(Pageable pageable) {

    Page<Employee> collection = employeeRepository.findAll(pageable);
    collection.forEach(
        e -> {
          try {
            e.add(
                linkTo(methodOn(EmployeeController.class).getEmployee(e.getEmployeeId(), assembler))
                    .withSelfRel());
          } catch (NotFoundException e1) {
            logger.warn(e1.getLocalizedMessage());
          }
        });
    return assembler.toResource(collection);
  }

  public Resource<Employee> getEmployee(Long id) throws NotFoundException {
    Optional<Employee> employeeOptional = employeeRepository.findById(id);

    if (!employeeOptional.isPresent()) {

      throw new NotFoundException("Can't find employee with ID :" + id);
    }
    Link linkToQueue =
        linkTo(methodOn(EmployeeController.class).getAllEmployees(PageRequest.of(0, 1), assembler))
            .withSelfRel();
    employeeOptional
        .get()
        .add(linkTo(methodOn(EmployeeController.class).getEmployee(id, assembler)).withSelfRel());
    return new Resource<>(employeeOptional.get(), linkToQueue);
  }

  public void deleteEmployee(Long id) {
    employeeRepository.deleteById(id);
  }

  public Resources<Employee> saveEmployee(Employee employee) throws NotFoundException {

    Employee savedEmployee = employeeRepository.save(employee);
    savedEmployee.add(
        linkTo(methodOn(EmployeeController.class).getEmployee(employee.getEmployeeId(), assembler))
            .withSelfRel());

    return new Resources<>(
        new ArrayList<>(Collections.singletonList(savedEmployee)),
        linkTo(methodOn(EmployeeController.class).getAllEmployees(PageRequest.of(0, 1), assembler))
            .withSelfRel());
  }

  public Resources<Employee> updateEmployee(Long id, Employee employee) throws NotFoundException {
    Optional<Employee> updatedEmployee = employeeRepository.findById(id);
    if (!updatedEmployee.isPresent()) {
      throw new NotFoundException("Can't find employee with ID :" + id);
    }
    employee.setEmployeeId(id);
    employeeRepository.save(employee);
    employee.add(
        linkTo(methodOn(EmployeeController.class).getEmployee(employee.getEmployeeId(), assembler))
            .withSelfRel());
    return new Resources<>(
        new ArrayList<>(Collections.singletonList(employee)),
        linkTo(methodOn(EmployeeController.class).getAllEmployees(PageRequest.of(0, 1), assembler))
            .withSelfRel());
  }
}
