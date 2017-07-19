package com.slabs.insight.web.controller.employee;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.slabs.insight.services.EmployeeService;

@Controller
public class EmployeeController {

	private EmployeeService employeeService;

	@Autowired
	public void setEmployeeResourceService(EmployeeService EmployeeResourceService) {
		this.employeeService = EmployeeResourceService;
	}

	@RequestMapping(value = "/employees", method = RequestMethod.GET)
	public String list(Model model) {
		List<EmployeeResource> lstEmployeeResources = employeeService.getAllEmployees();
		model.addAttribute("lstEmployees", lstEmployeeResources);
		System.out.println("Returning All EmployeeResources:" + lstEmployeeResources.size());
		return "employees";
	}

	@RequestMapping("employee/{id}")
	public String showEmployeeResourceById(@PathVariable Long employeeId, Model model) {
		EmployeeResource EmployeeResource = new EmployeeResource();
		EmployeeResource = employeeService.getEmployee(employeeId);
		List<EmployeeResource> lstEmployeeResources = new ArrayList<EmployeeResource>();
		lstEmployeeResources.add(EmployeeResource);
		model.addAttribute("lstEmployees", lstEmployeeResources);
		return "employees";
	}

	@RequestMapping("employee/edit/{employeeId}")
	public String edit(@PathVariable Long employeeId, Model model) {
		model.addAttribute("employeeDetails", employeeService.getEmployee(employeeId));
		return "employeeform";
	}

	@RequestMapping("employee/add")
	public String addNewEmployee(Model model) {
		model.addAttribute("employeeDetails", new EmployeeResource());
		return "employeeform";
	}

	@RequestMapping(value = "employee", method = RequestMethod.POST)
	public String createOrUpdateEmployeeResource(EmployeeResource EmployeeResource) {
		
		if(EmployeeResource.getEmployeeId() == null){
			employeeService.createEmployee(EmployeeResource);
		}
		else
		{
			employeeService.updateEmployee(EmployeeResource);
		}
		return "redirect:/employees";
	}

}