package mini.project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
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

	@BeforeEach
	void setUp() {
		userDao.deleteAll();
		userDao.save(new User("Bob", 2000));
		userDao.save(new User("Carl", 3000));
		userDao.save(new User("Dan", 4000));
		userDao.save(new User("Emily", 3500));
		userDao.save(new User("Alex", 2500));
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
		System.out.println("REULSTSSSSSSSS");
		mockMvc.perform(get("/users").param("min", Double.toString(min))).andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size()))).andDo(result -> {
					System.out.println(result);
				});
		System.out.println("RESULTSSSSSSSSSSS");
	}

	@Test
	void getUsersWorksThroughAllLayers_withMax() throws Exception {
		List<User> users = userDao.findAll();
		double max = 3500.0;
		List<User> expectedUsers = users.stream().filter(user -> user.getSalary() <= max).collect(Collectors.toList());
		mockMvc.perform(get("/users").param("max", Double.toString(max))).andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size()))).andDo(result -> {

				});
	}

	@Test
	void getUsersWorksThroughAllLayers_withLimit() throws Exception {
		List<User> users = userDao.findAll();
		int limit = 1;
		List<User> expectedUsers = users.subList(0, limit);
		System.out.println(expectedUsers);
		mockMvc.perform(get("/users").param("limit", Integer.toString(limit))).andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size()))).andDo(result -> {

				});
	}

	@Test
	void getUsersWorksThroughAllLayers_withSortName() throws Exception {
		List<User> users = userDao.findAll();
		List<User> expectedUsers = users.stream().sorted((user1,user2) -> user1.getName().compareTo(user2.getName())).collect(Collectors.toList());
		System.out.println(expectedUsers);
		mockMvc.perform(get("/users").param("min", "1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size()))).andDo(result -> {

				});
	}

}
