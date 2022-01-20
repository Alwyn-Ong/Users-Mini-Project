package mini.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Entity
@Table(name = "urls")
@ApiModel(description = "User Storage ")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(notes = "unique id of user")
	@JsonIgnore
	private int userId;

	@Column(name = "name")
	@ApiModelProperty(notes = "user's name")
	private String name;

	@Column(name = "salary")
	@ApiModelProperty(notes = "user's salary")
	private double salary;


	public User(String name, double salary) {
		super();
		this.name = name;
		this.salary = salary;
	}


	public User() {

	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getSalary() {
		return salary;
	}


	public void setSalary(double salary) {
		this.salary = salary;
	}


	@Override
	public String toString() {
		return "User [name=" + name + ", salary=" + salary + "]";
	}
	

}
