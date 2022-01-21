package mini.project.helper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import mini.project.exception.ParameterException;

public class OffsetBasedPageRequest implements Pageable, Serializable {

	private static final long serialVersionUID = -25822477129613575L;

	private final int limit;
	private final long offset;
	private final Sort sort;
	private static final Sort.Direction sortDirection = Sort.Direction.ASC;
	private static final Set<String> sortFields = new HashSet<>(Arrays.asList("name", "salary"));

	/**
	 * Creates a new {@link OffsetBasedPageRequest} with sort parameters applied.
	 *
	 * @param offset zero-based offset.
	 * @param limit  the size of the elements to be returned.
	 * @param sort   can be {@literal null}.
	 */
	public OffsetBasedPageRequest(long offset, int limit, Sort sort) {
		if (offset < 0) {
			throw new ParameterException("Offset index must not be less than zero!");
		}

		if (limit < 1) {
			throw new ParameterException("Limit must not be less than one!");
		}

		for (Sort.Order order : sort) {
			if (!sortFields.contains(order.getProperty())) {
				throw new ParameterException("Invalid Sort Field!");
			}
		}

		this.limit = limit;
		this.offset = offset;
		this.sort = sort;
	}

	/**
	 * Creates a new {@link OffsetBasedPageRequest} with sort parameters applied.
	 *
	 * @param offset     zero-based offset.
	 * @param limit      the size of the elements to be returned.
	 * @param direction  the direction of the {@link Sort} to be specified, can be
	 *                   {@literal null}.
	 * @param properties the properties to sort by, must not be {@literal null} or
	 *                   empty.
	 */
	public OffsetBasedPageRequest(long offset, int limit, Sort.Direction direction, String... properties) {
		this(offset, limit, Sort.by(direction, properties));
	}

	/**
	 * Creates a new {@link OffsetBasedPageRequest} with sort parameters applied.
	 * Defaults to ascending sort.
	 *
	 * @param offset     zero-based offset.
	 * @param limit      the size of the elements to be returned.
	 * @param properties the properties to sort by, must not be {@literal null} or
	 *                   empty.
	 */
	public OffsetBasedPageRequest(long offset, int limit, String... properties) {
		this(offset, limit, Sort.by(sortDirection, properties));
	}

	/**
	 * Creates a new {@link OffsetBasedPageRequest} with no sort parameters applied.
	 *
	 * @param offset zero-based offset.
	 * @param limit  the size of the elements to be returned.
	 */
	public OffsetBasedPageRequest(int offset, int limit) {
		this(offset, limit, Sort.unsorted());
	}

	@Override
	public int getPageNumber() {
		return Math.toIntExact(offset / limit);
	}

	@Override
	public int getPageSize() {
		return limit;
	}

	@Override
	public long getOffset() {
		return offset;
	}

	@Override
	public Sort getSort() {
		return sort;
	}

	@Override
	public Pageable next() {
		return new OffsetBasedPageRequest(getOffset() + getPageSize(), getPageSize(), getSort());
	}

	public OffsetBasedPageRequest previous() {
		return hasPrevious() ? new OffsetBasedPageRequest(getOffset() - getPageSize(), getPageSize(), getSort()) : this;
	}

	@Override
	public Pageable previousOrFirst() {
		return hasPrevious() ? previous() : first();
	}

	@Override
	public Pageable first() {
		return new OffsetBasedPageRequest(0, getPageSize(), getSort());
	}

	@Override
	public boolean hasPrevious() {
		return offset > limit;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof OffsetBasedPageRequest))
			return false;

		OffsetBasedPageRequest that = (OffsetBasedPageRequest) o;

		return new EqualsBuilder().append(limit, that.limit).append(offset, that.offset).append(sort, that.sort)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(limit).append(offset).append(sort).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("limit", limit).append("offset", offset).append("sort", sort)
				.toString();
	}
}