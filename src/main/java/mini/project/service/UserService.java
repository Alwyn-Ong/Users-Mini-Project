package mini.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import mini.project.dao.UserDao;
import mini.project.model.User;
import mini.project.model.UserParameters;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public ResponseEntity getUsers(UserParameters userParameters) {
		List<User> results = userDao.findAll(userParameters.getSpecification());
		
		return new ResponseEntity(results, HttpStatus.OK);
	}

}
