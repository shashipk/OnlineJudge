package com.infy.eta.databeans;

/**
 * Created by Amit Joshi on 10/22/2015.
 */
public class JudgeCategories {
	private Integer id;
	private String category;
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (category != null ? category.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JudgeCategories that = (JudgeCategories) o;

		if (id != that.id) return false;
		if (category != null ? !category.equals(that.category) : that.category != null) return false;
		if (description != null ? !description.equals(that.description) : that.description != null) return false;

		return true;
	}
}
