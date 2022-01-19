package mini.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import mini.project.dao.UserDao;

@Service
public class UserService {

	@Autowired
	private UserDao UserDao;

	public ResponseEntity getUsers(double min, double max, int offset, int limit, String sort) {
		System.out.println(min);
		System.out.println(max);
		System.out.println(offset);
		System.out.println(limit);
		System.out.println(sort);
		
		return new ResponseEntity("test", HttpStatus.OK);
	}

}
