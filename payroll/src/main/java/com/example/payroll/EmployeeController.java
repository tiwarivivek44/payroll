package com.example.payroll;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

	private final EmployeeRepository repository;
	
	// Constructor
	public EmployeeController(EmployeeRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping("/employees")
	public List<Employee> getAllEmployees(){
		return repository.findAll();
	}
	
	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee newEmp) {
		return repository.save(newEmp);
	}
	
	
	@GetMapping("/employees/{id}")
	public Employee getEmployee(@PathVariable Long id) {
		Employee employee = repository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(id));
		return employee;
	}

	/*
	@GetMapping("/employees/{id}")
	public EntityModel<Employee> getEmployee(@PathVariable Long id) {
		Employee employee = repository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(id));
		
		return EntityModel.of(employee, 
				linkTo(methodOn(EmployeeController.class).getEmployee(id)).withSelfRel(),
				linkTo(methodOn(EmployeeController.class).getAllEmployees()).withRel("employees"));
	}
	*/
	
	@PutMapping("/employees/{id}")
	public Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
		return repository.findById(id).map(emp ->{
			emp.setName(newEmployee.getName());
			emp.setRole(newEmployee.getRole());
			return repository.save(emp);
		}).orElseGet(() -> {
			newEmployee.setId(id);
			return repository.save(newEmployee);
		});
	}
	
	@DeleteMapping("/employees/{id}")
	public void deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
