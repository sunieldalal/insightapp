package com.slabs.insight.services;

import java.util.List;

import com.slabs.insight.web.controller.employee.EmployeeResource;

public interface EmployeeService {

	EmployeeResource createEmployee(EmployeeResource Employee);

	EmployeeResource getEmployee(Long pEmployeeId);

	List<EmployeeResource> getAllEmployees();

	EmployeeResource updateEmployee(EmployeeResource Employee);

	boolean deleteEmployee(Long pEmployeeId);

}
