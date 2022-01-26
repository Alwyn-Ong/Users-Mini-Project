package mini.project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
	 
	@BeforeEach
	void setUp() {
		userDao.deleteAll();
		
		testUsers.add(new User("Bob", 2000.01));
		testUsers.add(new User("Carl", 3000));
		testUsers.add(new User("Dan", 4000));
		testUsers.add(new User("Emily", 3500));
		testUsers.add(new User("Alex", 2500));
		
	}

	@AfterEach
	void tearDown() {
		userDao.deleteAll();
	}
	
	
	@Test
	@DisplayName("Upload CSV with only valid rows")
	void uploadCsvWorksThroughAllLayers() throws Exception {
		String fileName = "validData.csv";		
		is = this.getClass().getClassLoader().getResourceAsStream(fileName);		
		MockMultipartFile file = new MockMultipartFile("file", fileName, "multipart/form-data", is);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
				.file(file)
				.contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(jsonPath("$.success").value(1));
		
		List<User> addedUsers = userDao.findAll();
		System.out.println(addedUsers);
		System.out.println(testUsers);
	}
	
	@Test
	@DisplayName("Upload CSV with valid rows, existing rows and negative rows (criteria 2)")
	void uploadCsvWorksThroughAllLayers_criteria2() throws Exception {
		User existingUser = new User("Alex", 2000.00);
		User newUser = new User("Ben", 3500);
		User zeroSalaryUser = new User("David", 0.00);
		
		List<User> expectedUsers = new ArrayList<>();
		expectedUsers.add(existingUser);
		expectedUsers.add(newUser);
		expectedUsers.add(zeroSalaryUser);
				
		userDao.save(existingUser);
		
		String fileName = "validData_Crit2.csv";		
		is = this.getClass().getClassLoader().getResourceAsStream(fileName);		
		MockMultipartFile file = new MockMultipartFile("file", fileName, "multipart/form-data", is);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
				.file(file)
				.contentType(MediaType.MULTIPART_FORM_DATA))
		.andExpect(MockMvcResultMatchers.status().is(200))
		.andExpect(jsonPath("$.success").value(1));
		
		ResultActions actions = mockMvc.perform(get("/users")).andExpect(status().isOk())
				.andExpect(jsonPath("$.results", Matchers.hasSize(expectedUsers.size())));
		existingUser.setSalary(2111.00);
		UserControllerIntegrationTest.validateResponse(actions, expectedUsers);
		
	}
	
	@Test
	@DisplayName("Upload CSV with valid rows and a row with extra column")
	void uploadInvalidCsvShouldNotWork_extraCol() throws Exception {
		String fileName = "invalidData_extraCol.csv";		
		is = this.getClass().getClassLoader().getResourceAsStream(fileName);		
		MockMultipartFile file = new MockMultipartFile("file", fileName, "multipart/form-data", is);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
				.file(file)
				.contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.success").value(0));
	}
	
	@Test
	@DisplayName("Upload CSV with valid rows and a row with missing column")
	void uploadInvalidCsvShouldNotWork_missingCol() throws Exception {
		String fileName = "invalidData_missingCol.csv";		
		is = this.getClass().getClassLoader().getResourceAsStream(fileName);		
		MockMultipartFile file = new MockMultipartFile("file", fileName, "multipart/form-data", is);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
				.file(file)
				.contentType(MediaType.MULTIPART_FORM_DATA))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(jsonPath("$.success").value(0));
	}
	
	@Test
	@DisplayName("Upload CSV with valid rows and a row with salary as a string")
	void uploadInvalidCsvShouldNotWork_stringSalary() throws Exception {
		String fileName = "invalidData_stringSalary.csv";		
		is = this.getClass().getClassLoader().getResourceAsStream(fileName);		
		MockMultipartFile file = new MockMultipartFile("file", fileName, "multipart/form-data", is);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
				.file(file)
				.contentType(MediaType.MULTIPART_FORM_DATA))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(jsonPath("$.success").value(0));
	}
	

}

