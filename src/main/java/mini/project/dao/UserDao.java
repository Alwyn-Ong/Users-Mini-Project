package mini.project.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import mini.project.model.User;

// To access database
public interface UserDao extends JpaRepository<User, Integer> {

	User findByName(String name);
}