package com.slabs.insight.domain;

import org.springframework.beans.BeanUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.slabs.insight.web.controller.employee.EmployeeResource;

@DynamoDBTable(tableName = Employee.TABLE_NAME)
public class Employee {
	
	public static final String TABLE_NAME = "insightapp-employee";
	public static final String EMPLOYEE_ID = "employeeId";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String JOINING_DATE = "joiningDate";
	public static final String LOCATION = "location";
	
	private Long employeeId;
	
	private String firstName;
	
	private String lastName;
	
	private String joiningDate;
	
	private String location;
	
	public Employee() {
		//
	}
	
	public Employee(EmployeeResource employeeResource) {
		
		this.employeeId = employeeResource.getEmployeeId();
		this.firstName = employeeResource.getFirstName();
		this.lastName = employeeResource.getLastName();
		this.joiningDate = employeeResource.getJoiningDate();
		this.location = employeeResource.getLocation();
	}
	
	@DynamoDBHashKey(attributeName = Employee.EMPLOYEE_ID)
    public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long id) {
		this.employeeId = id;
	}
	
	@DynamoDBAttribute(attributeName = Employee.FIRST_NAME)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@DynamoDBAttribute(attributeName = Employee.LAST_NAME)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@DynamoDBAttribute(attributeName = Employee.JOINING_DATE)
	public String getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}

	@DynamoDBAttribute(attributeName = Employee.LOCATION)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	/*
	 * Copy bean properties to employeeResource
	 */
	public EmployeeResource toEmployeeResource(){
		
		EmployeeResource employeeResource = new EmployeeResource();
		BeanUtils.copyProperties(this, employeeResource);
		return employeeResource;
		
	}
}
