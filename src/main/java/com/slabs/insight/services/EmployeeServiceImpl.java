package com.slabs.insight.services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slabs.insight.domain.Employee;
import com.slabs.insight.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	private static SecureRandom random = new SecureRandom();
	
	@Autowired
	EmployeeRepository EmployeeRepo;

	@Override
	public Employee createEmployee(Employee EmployeeInfo) {
		
		// 
		EmployeeInfo.setEmployeeId(generateUniqueEmployeeId());
		
		LOGGER.info("Create new Employee {}" + EmployeeInfo.getEmployeeId());
		return EmployeeRepo.createEmployee(EmployeeInfo);
	}

	@Override
	public List<Employee> getAllEmployees() {

		LOGGER.info("Get All Employees");
		return EmployeeRepo.getAllEmployees();
	}

	@Override
	public Employee getEmployee(Long pEmployeeId) {

		LOGGER.info("Find Employee by Id {}" + pEmployeeId);
		return EmployeeRepo.getEmployeeById(pEmployeeId);
	}

	@Override
	public Employee updateEmployee(Employee pEmployeeInfo) {

		LOGGER.info("Update Employee by Id {}" + pEmployeeInfo.getEmployeeId());
		return EmployeeRepo.updateEmployee(pEmployeeInfo);
	}

	@Override
	public boolean deleteEmployee(Long pEmployeeInfo) {

		LOGGER.info("Delete Employee by Id {}" + pEmployeeInfo);
		return EmployeeRepo.deleteEmployeeById(pEmployeeInfo);
	}

	/*
	 * Helps generate unique UUID for employeeId
	 */
	public Long generateUniqueEmployeeId() {
		return Long.valueOf(new BigInteger(130, random).longValue());
	}

}
