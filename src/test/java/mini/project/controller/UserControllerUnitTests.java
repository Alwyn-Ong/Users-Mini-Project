package mini.project.controller;

import static org.mockito.Mockito.mockingDetails;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.annotation.After;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import mini.project.helper.UserParameters;
import mini.project.model.User;
import mini.project.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerUnitTests {

	@MockBean
	UserService userService;

	@Autowired
	MockMvc mockMvc;
	
	private ArrayList<User> testUsers;
	
	@BeforeEach
    void setUp() {
        testUsers = new ArrayList<>();
        testUsers.add(new User("Bob", 2000));
        testUsers.add(new User("Alice", 3000));
    }
	
	@AfterEach
	public void showInteractions() {
		System.out.println(mockingDetails(userService).getInvocations());
		System.out.println(mockingDetails(userService).getStubbings());		
	}
	
	private ResponseEntity generateResponse() {
		Map<String, List<User>> results = new HashMap<>();
		results.put("results", testUsers);
		System.out.println(results);
		return new ResponseEntity(results, HttpStatus.OK);
	}

	@Test
	public void testfindAll() throws Exception {
		User employee = new User("Bob", 2000);
		List<User> employees = Arrays.asList(employee);

		UserParameters userParameters = new UserParameters();
		System.out.println(userParameters);
		Mockito.when(userService.getUsers(new UserParameters())).thenReturn(generateResponse());

		mockMvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].name", Matchers.is("Bob")));
	}

}