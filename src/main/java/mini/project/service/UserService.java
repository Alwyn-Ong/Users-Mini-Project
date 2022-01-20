package mini.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import mini.project.dao.UserDao;
import mini.project.model.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public static Specification<User> withMin(Optional<Double> min) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("salary"), min.get());
	}

	public static Specification<User> withMax(Optional<Double> max) {
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("salary"), max.get());
	}

	public ResponseEntity getUsers(Optional<Double> min, Optional<Double> max, Optional<Integer> offset,
			Optional<Integer> limit, Optional<String> sort) {
//		System.out.println(min);
//		System.out.println(max);
//		System.out.println(offset);
//		System.out.println(limit);
//		System.out.println(sort);
		List<User> results = userDao.findAll(Specification.where(withMin(min).and(withMax(max))));
		Map<String, List<User>> message = new HashMap<>();
		System.out.println(results);
		return new ResponseEntity(results, HttpStatus.OK);
	}

}
