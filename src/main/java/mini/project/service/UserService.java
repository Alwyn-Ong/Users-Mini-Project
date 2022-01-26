package mini.project.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import mini.project.dao.UserDao;
import mini.project.helper.OffsetBasedPageRequest;
import mini.project.helper.UserParameters;
import mini.project.model.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public ResponseEntity getUsers(UserParameters userParameters) {
		Pageable offsetBasePageRequest;
		if (userParameters.getSort().isEmpty()) {
			offsetBasePageRequest = new OffsetBasedPageRequest(userParameters.getOffset().get(),
					userParameters.getLimit().get());
		} else {
			offsetBasePageRequest = new OffsetBasedPageRequest(userParameters.getOffset().get(),
					userParameters.getLimit().get(), userParameters.getSort().get().toLowerCase());
		}

		Page<User> resultsPaged = userDao.findAll(userParameters.getSpecification(), offsetBasePageRequest);
		Map<String, List<User>> results = new HashMap<>();
		results.put("results", resultsPaged.getContent());

		return new ResponseEntity(results, HttpStatus.OK);
	}

	private class UserBeanVerifier implements BeanVerifier<User> {

		@Override
		public boolean verifyBean(User user) throws CsvConstraintViolationException {

			if (user.getSalary().compareTo(new BigDecimal("0.00")) == -1) {
				// User salary is less than 0.00
				return false;
			}

			return true;
		}

	}

	private class CustomHeaderColumnNameMappingStrategy<T> extends HeaderColumnNameMappingStrategy {
		private String[] expectedHeadersOrdered = { "Name", "Salary" };

		@Override
		public void captureHeader(CSVReader reader) throws IOException, CsvRequiredFieldEmptyException {
			// Column Validation
			String[] actualCsvHeaders = reader.peek();
			String actualHeader, expectedHeader;
			if (expectedHeadersOrdered.length > actualCsvHeaders.length) {
				throw new CsvRequiredFieldEmptyException("Missing header column.");
			} else if (expectedHeadersOrdered.length < actualCsvHeaders.length) {
				throw new IOException("Unexpected extra header column.");
			}
			// Enforce strict column ordering with index
			for (int i = 0; i < actualCsvHeaders.length; i++) {
				actualHeader = actualCsvHeaders[i];
				expectedHeader = expectedHeadersOrdered[i];
				if (!expectedHeader.equals(actualHeader)) {
					throw new IOException("Header columns mismatch in ordering.");
				}
			}

			super.captureHeader(reader); // Back to default processing if the headers include ordering are as expected
		}
	}

	public ResponseEntity uploadUsers(MultipartFile document) {
		Map<String, Object> results = new HashMap<>();
		int isSuccessInt = 1;
		HttpStatus returnStatus = HttpStatus.OK;

		// parse CSV file to create a list of `User` objects
		try (Reader reader = new BufferedReader(new InputStreamReader(document.getInputStream()))) {

			CustomHeaderColumnNameMappingStrategy mappingStrategy = new CustomHeaderColumnNameMappingStrategy<User>();
		    mappingStrategy.setType(User.class);
			// create csv bean reader
			List<User> users = new CsvToBeanBuilder(reader)
					.withType(User.class)
					.withIgnoreLeadingWhiteSpace(true)
					.withMappingStrategy(mappingStrategy)
					.withVerifier(new UserBeanVerifier())
					.build()
					.parse();

			for (User user : users) {
				User existing = userDao.findByName(user.getName());
				if (existing != null) {
					existing.setSalary(user.getSalary());
					userDao.save(existing);
				} else {
					userDao.save(user);
				}
			}

		} catch (Exception ex) {
			if (ex.getCause() instanceof CsvException) {
				// Error parsing csv file
				returnStatus = HttpStatus.BAD_REQUEST;
			} else {
				returnStatus = HttpStatus.BAD_GATEWAY;
			}

			isSuccessInt = 0;
			results.put("error", ex.getMessage());
		}

		results.put("success", isSuccessInt);
		return new ResponseEntity(results, returnStatus);
	}

}
