package com.infy.eta.databeans;



/**
 * Created by Amit Joshi on 10/17/2015.
 */
public class JudgeSubcategoriesEntity {
	private Integer id;
	private String subcategory;
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (subcategory != null ? subcategory.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JudgeSubcategoriesEntity that = (JudgeSubcategoriesEntity) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (subcategory != null ? !subcategory.equals(that.subcategory) : that.subcategory != null) return false;
		if (description != null ? !description.equals(that.description) : that.description != null) return false;

		return true;
	}
}
