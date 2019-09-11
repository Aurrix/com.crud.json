package com.crud.jason;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.crud.jason.config.ExceptionEntity;
import com.crud.jason.entities.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;
	
	@LocalServerPort
	int port;
	
	String rootUrl() {
		return "http://localhost:"+port;
	}
	
	@Test
	public void getAllEmployeesTest() {
			
			HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
			ResponseEntity<?> response = restTemplate.exchange(rootUrl()+"/employees", HttpMethod.GET, entity,Resources.class);
			String result = response.getBody().toString();
			assertTrue(result.contains("_links"));
			assertTrue(result.contains("self"));
			assertTrue(result.contains("employee"));
			assertTrue(result.contains("page"));
			assertTrue(response.getStatusCode()==HttpStatus.OK);
			assertNotNull(response.getBody());
			
 	}

	@Test
	public void getEmployeeByIdTest() {
		int id = 1;
		Employee response = restTemplate.getForObject(rootUrl()+"/employee/"+id, Employee.class);
		assertNotNull(response);
		System.out.println(response.getName());
		assertTrue(response.getName().equals("Isaiah"));
		
	}
	
	@Test
	public void postEmployeeTest () {
		 	Employee employee = new Employee();
	        employee.setEmail("someTestEmail@gmail.com");
	        employee.setName("TestDude");
	        employee.setSurname("HisSurname");
	        ResponseEntity<Employee> postResponse = restTemplate.postForEntity(rootUrl() + "/employees", employee, Employee.class);
	        assertNotNull(postResponse);
	        assertNotNull(postResponse.getBody());
	        assertTrue(postResponse.getStatusCode()==HttpStatus.OK);
	}
	
	@Test
	public void putEmployeeTest() {
		
	}
	
	@Test
	public void deleteEmployeeTest() {
		
	}
	
}
