package mini.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mini.project.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/")
public class UserController {

	@Autowired
	private UserService service;

	// To get a list of users based on params
	@GetMapping("/users")
	public ResponseEntity getUsers(@RequestParam("min") double min, @RequestParam("max") double max, @RequestParam("offset") int offset, @RequestParam("limit") int limit, @RequestParam("sort") String sort) {
		return service.getUsers(min, max, offset, limit, sort);
	}

}
