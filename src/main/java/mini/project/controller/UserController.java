package mini.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mini.project.helper.UserParameters;
import mini.project.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/")
public class UserController {

	@Autowired
	private UserService service;

	// To get a list of users based on params
	@GetMapping("/users")
	public ResponseEntity getUsers(UserParameters userParameters) {
		return service.getUsers(userParameters);
	}
	
	@PostMapping(path="/upload", consumes= {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity uploadUsers(@RequestPart MultipartFile document) {
		return service.uploadUsers(document);
	}

}
