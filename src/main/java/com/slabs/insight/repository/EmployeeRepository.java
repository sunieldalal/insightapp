package com.slabs.insight.repository;

import java.util.List;

import com.slabs.insight.domain.Employee;

public interface EmployeeRepository {

	Employee createEmployee(Employee Employee);
	
	Employee getEmployeeById(Long IdPersonId);
	
	List<Employee> getAllEmployees();
	
	Employee updateEmployee(Employee Employee);
	
	boolean deleteEmployeeById(Long IdPersonId);

}