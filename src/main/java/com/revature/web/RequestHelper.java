package com.revature.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.EmployeeDao;
import com.revature.models.Employee;
import com.revature.service.EmployeeService;

public class RequestHelper {

	private static Logger log = Logger.getLogger(RequestHelper.class);
	private static EmployeeService eserv = new EmployeeService(new EmployeeDao());
	private static ObjectMapper om = new ObjectMapper();

	public static void processEmployees(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		response.setContentType("test/html");

		// 2. get the list of all employees in the database
		List<Employee> allEmps = eserv.findAll(); // create this method in the service layer

		// 3. Turn the list of Java Objects into a JSON string
		String json = om.writeValueAsString(allEmps);

		// 4. Use a Print Writer to write the objects to the response body seen in the
		// browser
		PrintWriter out = response.getWriter();
		out.println(json);

	}

	public static void processLogin(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// we need to capture the user input and split up the username and password
		BufferedReader reader = request.getReader();

		StringBuilder s = new StringBuilder();

		// transfer everything over
		String line = reader.readLine();

		while (line != null) {

			s.append(line);
			line = reader.readLine();

		}

		String body = s.toString();
		String[] sepByAmp = body.split("&");

		List<String> values = new ArrayList<String>();

		for (String pair : sepByAmp) {

			values.add(pair.substring(pair.indexOf("=") + 1));

		}

		// capture the actual username and password values
		String username = values.get(0);
		String password = values.get(1);

		log.info("User attempted to login with username" + username);

		// call the confirmLogin() method!
		Employee e = eserv.confirmLogin(username, password);

		if (e != null) {

			HttpSession session = request.getSession();
			session.setAttribute("user", e);

			// print the logged in user to the screen
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");

			out.println(om.writeValueAsString(e));

			log.info("The user " + username + " has logged in.");
		} else {
			response.setStatus(204);
		}
	}

	public static void processError(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// If something goes wrong, redirect the user to a custom 404 error page
		request.getRequestDispatcher("error.html").forward(request, response);
	}
}