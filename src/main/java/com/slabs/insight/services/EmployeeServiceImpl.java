package com.slabs.insight.services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slabs.insight.domain.Employee;
import com.slabs.insight.repository.EmployeeRepository;
import com.slabs.insight.web.controller.employee.EmployeeResource;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	private static SecureRandom random = new SecureRandom();
	
	@Autowired
	EmployeeRepository EmployeeRepo;

	@Override
	public EmployeeResource createEmployee(EmployeeResource EmployeeInfo) {
		
		EmployeeInfo.setEmployeeId(generateUniqueEmployeeId());
		
		LOGGER.info("Create new Employee {}" + EmployeeInfo.getEmployeeId());
		return EmployeeRepo.createEmployee(new Employee(EmployeeInfo)).toEmployeeResource();
	}

	@Override
	public List<EmployeeResource> getAllEmployees() {

		LOGGER.info("Get All Employees");
		List<Employee> lstEmployee = EmployeeRepo.getAllEmployees();
		
		List<EmployeeResource> lstEmployeeResource = new ArrayList<EmployeeResource>();
		
		for(Employee emp : lstEmployee){
			EmployeeResource empResource = new EmployeeResource();
			BeanUtils.copyProperties(emp, empResource);
			lstEmployeeResource.add(empResource);
		}
		return lstEmployeeResource;
	}

	@Override
	public EmployeeResource getEmployee(Long pEmployeeId) {

		LOGGER.info("Find Employee by Id {}" + pEmployeeId);
		return EmployeeRepo.getEmployeeById(pEmployeeId).toEmployeeResource();
	}

	@Override
	public EmployeeResource updateEmployee(EmployeeResource pEmployeeInfo) {

		LOGGER.info("Update Employee by Id {}" + pEmployeeInfo.getEmployeeId());
		return EmployeeRepo.updateEmployee(new Employee(pEmployeeInfo)).toEmployeeResource();
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
		return Long.valueOf(new BigInteger(30, random).longValue());
	}

}
