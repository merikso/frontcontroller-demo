package com.revature.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revature.dao.EmployeeDao;
import com.revature.models.Employee;
import com.revature.service.EmployeeService;

public class EmployeeServiceTests {
	
	EmployeeDao mockdao = new EmployeeDao();
	EmployeeService eserv = new EmployeeService(mockdao);
	
	
	
	
	@Before
	public void setup() {
		
		mockdao = mock(EmployeeDao.class);
		eserv = new EmployeeService(mockdao);
		
	}
	@After
	public void teardown() {
		
		eserv = null;
		mockdao = null;
		
	}
	@Test
	public void testConfirmationLogin_success() {
		
		Employee e1 = new Employee(3, "Scott", "Lang", "Antman", "bugs");
		Employee e2 = new Employee(43, "Clint", "Barton", "Hawkeye", "arrows");
		
		List<Employee> dummyDb = new ArrayList<>();
		dummyDb.add(e1);
		dummyDb.add(e2);
		
		when(mockdao.findAll()).thenReturn(dummyDb);
		
		assertEquals(e2, eserv.confirmLogin("Hawkeye", "arrows"));
		
	}
	
	@Test
	public void testFailConfirmLogin_returnNull() {
		
		Employee e1 = new Employee(3, "Scott", "Lang", "Antman", "bugs");
		Employee e2 = new Employee(43, "Clint", "Barton", "Hawkeye", "arrows");
		
		List<Employee> dummyDb = new ArrayList<>();
		dummyDb.add(e1);
		dummyDb.add(e2);
		
		when(mockdao.findAll()).thenReturn(dummyDb);
		

		assertEquals(null, eserv.confirmLogin("Antman", "buggy"));
		
	}
	
	
	
	
	
	
}
