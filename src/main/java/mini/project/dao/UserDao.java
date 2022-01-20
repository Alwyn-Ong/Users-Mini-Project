package mini.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import mini.project.model.User;

// To access database
public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

	User findByName(String name);
}