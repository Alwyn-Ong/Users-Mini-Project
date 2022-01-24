package mini.project.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import mini.project.dao.UserDao;
import mini.project.model.User;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserDao userDao;

	private List<User>testUsers = new ArrayList<>();
	
	@BeforeEach
	void setUp() {
		userDao.deleteAll();
		
		testUsers.add(new User("Bob", 2000));
		testUsers.add(new User("Carl", 3000));
		testUsers.add(new User("Dan", 4000));
		testUsers.add(new User("Emily", 3500));
		testUsers.add(new User("Alex", 2500));
		
		userDao.saveAll(testUsers);
	}

	@AfterEach
	void tearDown() {
		userDao.deleteAll();
	}
	
	@Test
	void getUsersWorksThroughAllLayers() throws Exception {
		List<User> expectedUsers = userDao.findAll();
		mockMvc.perform(get("/users")).andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size()))).andDo(result -> {

				});
	}

	@Test
	void getUsersWorksThroughAllLayers_withMin() throws Exception {
		List<User> users = userDao.findAll();
		double min = 2500.0;
		List<User> expectedUsers = users.stream().filter(user -> user.getSalary() >= min).collect(Collectors.toList());
		ResultActions actions = mockMvc.perform(get("/users")
				.param("min", Double.toString(min)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size())));
		
		for (int i = 0; i < expectedUsers.size(); i++) {
			actions = actions.andExpect(jsonPath(String.format("$.results[%s].name",i), is(expectedUsers.get(i).getName())));
			actions = actions.andExpect(jsonPath(String.format("$.results[%s].salary",i), is(expectedUsers.get(i).getSalary())));
		}
		
	}

	@Test
	void getUsersWorksThroughAllLayers_withMax() throws Exception {
		List<User> users = userDao.findAll();
		double max = 3500.0;
		List<User> expectedUsers = users.stream().filter(user -> user.getSalary() <= max).collect(Collectors.toList());
		ResultActions actions = mockMvc.perform(get("/users")
				.param("max", Double.toString(max)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size())));
		
		for (int i = 0; i < expectedUsers.size(); i++) {
			actions = actions.andExpect(jsonPath(String.format("$.results[%s].name",i), is(expectedUsers.get(i).getName())));
			actions = actions.andExpect(jsonPath(String.format("$.results[%s].salary",i), is(expectedUsers.get(i).getSalary())));
		}
		
	}

	@Test
	void getUsersWorksThroughAllLayers_withLimit() throws Exception {
		List<User> users = userDao.findAll();
		int limit = 1;
		List<User> expectedUsers = users.subList(0, limit);
		ResultActions actions = mockMvc.perform(get("/users").param("limit", Integer.toString(limit)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size())));
		
		for (int i = 0; i < expectedUsers.size(); i++) {
			actions = actions.andExpect(jsonPath(String.format("$.results[%s].name",i), is(expectedUsers.get(i).getName())));
			actions = actions.andExpect(jsonPath(String.format("$.results[%s].salary",i), is(expectedUsers.get(i).getSalary())));
		}
	}

	@Test
	void getUsersWorksThroughAllLayers_withSortName() throws Exception {
		List<User> users = userDao.findAll();
		List<User> expectedUsers = users.stream().sorted((user1,user2) -> user1.getName().compareTo(user2.getName())).collect(Collectors.toList());
		ResultActions actions = mockMvc.perform(get("/users")
				.param("min", "1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size())));
		
		for (int i = 0; i < expectedUsers.size(); i++) {
			actions = actions.andExpect(jsonPath(String.format("$.results[%s].name",i), is(expectedUsers.get(i).getName())));
			actions = actions.andExpect(jsonPath(String.format("$.results[%s].salary",i), is(expectedUsers.get(i).getSalary())));
		}
	}

}
