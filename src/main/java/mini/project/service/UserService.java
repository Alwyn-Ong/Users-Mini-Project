package mini.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import mini.project.dao.UserDao;
import mini.project.helper.OffsetBasedPageRequest;
import mini.project.helper.UserParameters;
import mini.project.model.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public ResponseEntity getUsers(UserParameters userParameters) {
		// TODO: input validation
		Pageable offsetBasePageRequest;
		if (userParameters.getSort().isEmpty()) {
			offsetBasePageRequest = new OffsetBasedPageRequest(userParameters.getOffset().get(),
					userParameters.getLimit().get());
		} else {
			offsetBasePageRequest = new OffsetBasedPageRequest(userParameters.getOffset().get(),
					userParameters.getLimit().get(), userParameters.getSort().get().toLowerCase());
		}

		Page<User> resultsPaged = userDao.findAll(userParameters.getSpecification(), offsetBasePageRequest);
		List<User> results = resultsPaged.getContent();
		return new ResponseEntity(results, HttpStatus.OK);
	}

}
