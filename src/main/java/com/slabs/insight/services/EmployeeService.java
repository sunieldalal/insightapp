package com.slabs.insight.services;

import java.util.List;

import com.slabs.insight.domain.Employee;

public interface EmployeeService {

	Employee createEmployee(Employee Employee);

	Employee getEmployee(Long pEmployeeId);

	List<Employee> getAllEmployees();

	Employee updateEmployee(Employee Employee);

	boolean deleteEmployee(Long pEmployeeId);

}
