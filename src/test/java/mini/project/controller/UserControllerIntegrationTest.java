package mini.project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import mini.project.dao.UserDao;
import mini.project.model.User;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserDao userDao;

	@Test
	void getUsersWorksThroughAllLayers() throws Exception {
//		User user = new User("Alice", 1000.00);
//		userDao.save(user);
		mockMvc.perform(get("/users").param("min", "1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(5)));
	}
}
