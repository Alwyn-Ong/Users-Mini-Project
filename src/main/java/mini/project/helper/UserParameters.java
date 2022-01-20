package mini.project.helper;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import lombok.Data;
import lombok.NoArgsConstructor;
import mini.project.model.User;

@Data
@NoArgsConstructor
public class UserParameters {
	// Open for future (optional) params
	private Optional<Double> min = Optional.of(0.0);
	private Optional<Double> max = Optional.of(4000.0);
	private Optional<Integer> offset = Optional.of(0);
	private Optional<Integer> limit= Optional.of(Integer.MAX_VALUE);
	private Optional<String> sort = Optional.empty();
	
	
	
	public Optional<Double> getMin() {
		return min;
	}



	public void setMin(Optional<Double> min) {
		this.min = min;
	}



	public Optional<Double> getMax() {
		return max;
	}



	public void setMax(Optional<Double> max) {
		this.max = max;
	}



	public Optional<Integer> getOffset() {
		return offset;
	}



	public void setOffset(Optional<Integer> offset) {
		this.offset = offset;
	}



	public Optional<Integer> getLimit() {
		return limit;
	}



	public void setLimit(Optional<Integer> limit) {
		this.limit = limit;
	}



	public Optional<String> getSort() {
		return sort;
	}



	public void setSort(Optional<String> sort) {
		this.sort = sort;
	}



	@Override
	public String toString() {
		return "UserParameters [min=" + min + ", max=" + max + ", offset=" + offset + ", limit=" + limit + ", sort="
				+ sort + "]";
	}
	
	public Specification<User> withMin() {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("salary"), this.min.get());
	}

	public Specification<User> withMax() {
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("salary"), this.max.get());
	}
	
		
	public Specification<User> getSpecification(){
		return Specification.where(this.withMax().and(this.withMin()));
	}
	
	
}
