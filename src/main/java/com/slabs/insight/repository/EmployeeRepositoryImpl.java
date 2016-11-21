package com.slabs.insight.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.slabs.insight.domain.Employee;

@Service
public class EmployeeRepositoryImpl implements EmployeeRepository {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class.getName());

  private AmazonDynamoDBClient dynamoDBClient;

  @Autowired
  public EmployeeRepositoryImpl(AmazonDynamoDBClient amazonDynamoDBClientService) {
	this.dynamoDBClient = amazonDynamoDBClientService;
  }

 @Override
 public Employee createEmployee(Employee pEmployee) {

	DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
	LOGGER.info("Create Employee with Employee Id: " + pEmployee.getEmployeeId());
	Employee employee = null;

	try {

		employee = mapper.load(Employee.class, pEmployee.getEmployeeId());

		// If Employee record does not exist, create new Employee
		if (employee == null) {

			employee = new Employee();
			employee.setEmployeeId(pEmployee.getEmployeeId());
			employee.setFirstName(pEmployee.getFirstName());
			employee.setLastName(pEmployee.getLastName());
			employee.setJoiningDate(pEmployee.getJoiningDate());
			employee.setLocation(pEmployee.getLocation());

			// Update the database
			mapper.save(employee);
			LOGGER.info("Employee " + pEmployee.getEmployeeId() + " created successfully.");
		} else {
			// update the existing Employee
			updateEmployee(pEmployee);
			LOGGER.info("Employee " + pEmployee.getEmployeeId() + " updated successfully.");
		}

	} catch (AmazonClientException ex) {
		LOGGER.error("Problem Connecting to DynamoDb: " + ex.getMessage());
		throw new RuntimeException(ex.getMessage(), ex);
	}

	return employee;
}

 @Override
 public Employee getEmployeeById(Long IdEmployeeId) {

	DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
	LOGGER.info("Fetch Employee with EmployeeId: " + IdEmployeeId.longValue());
	Employee EmployeeFromDB = null;

	try {
		EmployeeFromDB = mapper.load(Employee.class, IdEmployeeId);
	} catch (AmazonClientException ex) {
		LOGGER.error("Problem Connecting to DynamoDb: " + ex.getMessage());
		throw new RuntimeException(ex.getMessage(), ex);
	}
	return EmployeeFromDB;
  }

  @Override
  public List<Employee> getAllEmployees() {

    List<Employee> lstEmployees = new ArrayList<Employee>();

	try {

	  ScanRequest scanRequest = new ScanRequest().withTableName(Employee.TABLE_NAME);
	  ScanResult result = null;

	  LOGGER.info("Fetching data for All employees");

	/*
	 * The data returned from a Query or Scan operation is limited to 1
	 * MB; this means that if you scan a table that has more than 1 MB
	 * of data, you’ll need to perform another Scan operation to
	 * continue to the next 1 MB of data in the table. If you query for
	 * specific attributes that match values that amount to more than 1
	 * MB of data, you’ll need to perform another Query request for the
	 * next 1 MB of data. The second query request uses a starting point
	 * (ExclusiveStartKey) based on the key of the last returned value
	 * (LastEvaluatedKey) so you can progressively query or scan for new
	 * data in 1 MB increments. The LastEvaluatedKey is null when the
	 * entire Query or Scan result set is complete (i.e. the operation
	 * processed the “last page”).
	*/
	  do {

		if (result != null) {
			scanRequest.setExclusiveStartKey(result.getLastEvaluatedKey());
		}

		result = dynamoDBClient.scan(scanRequest);

		List<Map<String, AttributeValue>> rows = result.getItems();

		// Iterate through All rows
		for (Map<String, AttributeValue> mapEmployeeRecord : rows) {
			Employee employee = parseEmployeeInfo(mapEmployeeRecord);
			lstEmployees.add(employee);
		}

	  } while (result.getLastEvaluatedKey() != null);

	  LOGGER.info("Total Employee Records in database=" + lstEmployees.size());

      } catch (AmazonClientException ex) {
	    LOGGER.error("Problem Connecting to DynamoDb: " + ex.getMessage());
	    throw new RuntimeException(ex.getMessage(), ex);
    }

    LOGGER.info("All employees data returned successfully.");
    return lstEmployees;
  }

  /*
   * Converts Map of Attribute values to Employee Domain Object
   */
  private Employee parseEmployeeInfo(Map<String, AttributeValue> mapEmployeeRecord) {

	Employee employee = new Employee();

	try {

		AttributeValue employeeIdAttrValue = mapEmployeeRecord.get(Employee.EMPLOYEE_ID);
		AttributeValue firstNameAttrValue = mapEmployeeRecord.get(Employee.FIRST_NAME);
		AttributeValue lastNameAttrValue = mapEmployeeRecord.get(Employee.LAST_NAME);
		AttributeValue joiningDateAttrValue = mapEmployeeRecord.get(Employee.JOINING_DATE);
		AttributeValue locationAttrValue = mapEmployeeRecord.get(Employee.LOCATION);

		// Being primary key, should always be non null
		employee.setEmployeeId(Long.valueOf(employeeIdAttrValue.getN()));

		if (firstNameAttrValue != null) {
			employee.setFirstName(mapEmployeeRecord.get(Employee.FIRST_NAME).getS());
		}

		if (lastNameAttrValue != null) {
			employee.setLastName(mapEmployeeRecord.get(Employee.LAST_NAME).getS());
		}

		if (joiningDateAttrValue != null) {
			employee.setJoiningDate(mapEmployeeRecord.get(Employee.JOINING_DATE).getS());
		}

		if (locationAttrValue != null) {
			employee.setLocation(mapEmployeeRecord.get(Employee.LOCATION).getS());
		}

	} catch (NumberFormatException ex) {
		System.out.println(ex.getMessage());
	}

	return employee;
  }

  @Override
  public Employee updateEmployee(Employee pEmployee) {

	DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
	LOGGER.info("Update Employee with EmployeeId: " + pEmployee.getEmployeeId());
	Employee employee = null;

	try {

		employee = mapper.load(Employee.class, pEmployee.getEmployeeId());

		// If Employee record exist, update the Employee
		if (employee != null) {
			employee.setEmployeeId(pEmployee.getEmployeeId());
			employee.setFirstName(pEmployee.getFirstName());
			employee.setLastName(pEmployee.getLastName());
			employee.setJoiningDate(pEmployee.getJoiningDate());
			employee.setLocation(pEmployee.getLocation());
		}

		// Update the database
		mapper.save(employee);
		LOGGER.info("Employee updated successfully.");

	} catch (AmazonClientException ex) {
		LOGGER.error("Problem Connecting to DynamoDb: " + ex.getMessage());
		throw new RuntimeException(ex.getMessage(), ex);
	}

	return employee;
  }

  @Override
  public boolean deleteEmployeeById(Long pEmployeeId) {

	DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);
	LOGGER.info("delete Employee with Employee Id: " + pEmployeeId);
	Employee employee = null;

	try {

		employee = mapper.load(Employee.class, pEmployeeId);

		if (employee != null) {
			mapper.delete(employee);
			LOGGER.info("Employee " + pEmployeeId + " deleted successfully.");
		} else {
			LOGGER.info("Employee " + pEmployeeId + " not present in database.");
		}

	} catch (AmazonClientException ex) {
		LOGGER.error("Problem Connecting to DynamoDb: " + ex.getMessage());
		throw new RuntimeException(ex.getMessage(), ex);
	}

	return true;
  }
}