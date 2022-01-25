package mini.project.controller;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.test.web.servlet.ResultActions;

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
		
		testUsers.add(new User("Bob", 2000.01));
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
	
	public static void validateResponse(ResultActions actions, List<User> expectedUsers) throws Exception {
		for (int i = 0; i < expectedUsers.size(); i++) {
			actions = actions.andExpect(jsonPath(String.format("$.results[%s].name",i), is(expectedUsers.get(i).getName())));
			actions = actions.andExpect(jsonPath(String.format("$.results[%s].salary",i), Matchers.closeTo(expectedUsers.get(i).getSalary(), new BigDecimal("0.00")), BigDecimal.class));
		}
	}
	
	@Test
	void getUsersWorksThroughAllLayers() throws Exception {
		List<User> expectedUsers = userDao.findAll();
		ResultActions actions = mockMvc.perform(get("/users")).andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size())));
		validateResponse(actions, expectedUsers);
	}

	@Test
	void getUsersWorksThroughAllLayers_withMin() throws Exception {
		List<User> users = userDao.findAll();
		double min = 2500.0;
		List<User> expectedUsers = users.stream().filter(user -> user.getSalary().compareTo(new BigDecimal(min)) >= 0).collect(Collectors.toList());
		ResultActions actions = mockMvc.perform(get("/users")
				.param("min", Double.toString(min)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size())));
		validateResponse(actions, expectedUsers);		
	}

	@Test
	void getUsersWorksThroughAllLayers_withMax() throws Exception {
		List<User> users = userDao.findAll();
		double max = 3500.0;
		List<User> expectedUsers = users.stream().filter(user -> user.getSalary().compareTo(new BigDecimal(max)) <= 0).collect(Collectors.toList());
		ResultActions actions = mockMvc.perform(get("/users")
				.param("max", Double.toString(max)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size())));
		validateResponse(actions, expectedUsers);
		
	}

	@Test
	void getUsersWorksThroughAllLayers_withLimit() throws Exception {
		List<User> users = userDao.findAll();
		int limit = 1;
		List<User> expectedUsers = users.subList(0, limit);
		ResultActions actions = mockMvc.perform(get("/users").param("limit", Integer.toString(limit)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size())));
		validateResponse(actions, expectedUsers);
	}

	@Test
	void getUsersWorksThroughAllLayers_withSortName() throws Exception {
		List<User> users = userDao.findAll();
		List<User> expectedUsers = users.stream().sorted((user1,user2) -> user1.getName().compareTo(user2.getName())).collect(Collectors.toList());
		ResultActions actions = mockMvc.perform(get("/users")
				.param("sort", "name")).andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size())));
		validateResponse(actions, expectedUsers);
	}
	
	@Test
	void getUsersWorksThroughAllLayers_withSortSalary() throws Exception {
		List<User> users = userDao.findAll();
		List<User> expectedUsers = users.stream().sorted((user1,user2) -> user1.getSalary().compareTo(user2.getSalary())).collect(Collectors.toList());
		ResultActions actions = mockMvc.perform(get("/users")
				.param("sort", "salary")).andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size())));
		validateResponse(actions, expectedUsers);
	}
	
	@Test
	void getUsersDoesNotWork_withInvalidSort() throws Exception {
		mockMvc.perform(get("/users")
				.param("sort", "invalid_sort"))
				.andExpect(status().isBadRequest());
	}

}
