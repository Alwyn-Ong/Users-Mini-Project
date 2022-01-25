package mini.project.controller;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import mini.project.dao.UserDao;
import mini.project.model.User;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UploadIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserDao userDao;

	private List<User>testUsers = new ArrayList<>();
	
	@Spy
    @InjectMocks
    private UserController controller = new UserController();
	
	private InputStream is;
	 
	@Before(value = "")
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        is = controller.getClass().getClassLoader().getResourceAsStream("test.csv");
        
		try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader reader = new BufferedReader(streamReader)) {

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

    }
	
	@BeforeEach
	void setUp() {
		userDao.deleteAll();
		
		testUsers.add(new User("Bob", 2000.01));
		testUsers.add(new User("Carl", 3000));
		testUsers.add(new User("Dan", 4000));
		testUsers.add(new User("Emily", 3500));
		testUsers.add(new User("Alex", 2500));
		
//		userDao.saveAll(testUsers);
		
		is = controller.getClass().getClassLoader().getResourceAsStream("test.csv");
	}

	@AfterEach
	void tearDown() {
		userDao.deleteAll();
	}
	
	private void validateResponse(ResultActions actions, List<User> expectedUsers) throws Exception {
		for (int i = 0; i < expectedUsers.size(); i++) {
			actions = actions.andExpect(jsonPath(String.format("$.results[%s].name",i), is(expectedUsers.get(i).getName())));
			actions = actions.andExpect(jsonPath(String.format("$.results[%s].salary",i), Matchers.closeTo(expectedUsers.get(i).getSalary(), new BigDecimal("0.00")), BigDecimal.class));
		}
	}
	
	// https://www.baeldung.com/spring-multipart-post-request-test
	// https://roytuts.com/junit-testing-of-file-upload-and-download-in-spring-rest-controllers/
	@Test 
	void uploadCsvWorksThroughAllLayers() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "test.csv", "multipart/form-data", is);
//		mockMvc.perform(multipart("/upload").file(file)).andExpect(status().isOk());
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(file).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
		
		List<User> addedUsers = userDao.findAll();
		System.out.println(addedUsers);
		System.out.println(testUsers);
		
	}
	

}

