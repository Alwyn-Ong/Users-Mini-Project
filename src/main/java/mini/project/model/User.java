package mini.project.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
@ApiModel(description = "User Storage ")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(notes = "unique id of user")
	@JsonIgnore
	private int userId;

	@Column(name = "name", unique = true)
	@ApiModelProperty(notes = "user's name")
	@CsvBindByName
	private String name;

	@Column(name = "salary", precision=8, scale=2)
	@ApiModelProperty(notes = "user's salary")
	@CsvBindByName
	private BigDecimal salary;

	public User(String name, BigDecimal salary) {
		super();
		this.name = name;
		this.salary = salary;
	}

	public User() {

	}
	
	public User(String name, double salary) {
		super();
		this.name = name;
		this.salary = new BigDecimal(salary).setScale(2, RoundingMode.HALF_UP);
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

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}
	
	public void setSalary(double salary) {
		this.salary = new BigDecimal(salary).setScale(2, RoundingMode.HALF_UP);
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", salary=" + salary + "]";
	}

}
