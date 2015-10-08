package com.infy.eta.databeans;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * Created by Amit Joshi on 10/4/2015.
 */
@XmlRootElement
public class JudgeProblemsEntity {
	private Integer id;
	private String title;
	private String category;
	private String subcategory;
	private String problemStatement;
	private String input;
	private String constraints;
	private String output;
	private Collection<JudgeTestCasesEntity> judgeTestCasesById;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public String getProblemStatement() {
		return problemStatement;
	}

	public void setProblemStatement(String problemStatement) {
		this.problemStatement = problemStatement;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getConstraints() {
		return constraints;
	}

	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (category != null ? category.hashCode() : 0);
		result = 31 * result + (subcategory != null ? subcategory.hashCode() : 0);
		result = 31 * result + (problemStatement != null ? problemStatement.hashCode() : 0);
		result = 31 * result + (input != null ? input.hashCode() : 0);
		result = 31 * result + (constraints != null ? constraints.hashCode() : 0);
		result = 31 * result + (output != null ? output.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JudgeProblemsEntity that = (JudgeProblemsEntity) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (title != null ? !title.equals(that.title) : that.title != null) return false;
		if (category != null ? !category.equals(that.category) : that.category != null) return false;
		if (subcategory != null ? !subcategory.equals(that.subcategory) : that.subcategory != null) return false;
		if (problemStatement != null ? !problemStatement.equals(that.problemStatement) : that.problemStatement != null)
			return false;
		if (input != null ? !input.equals(that.input) : that.input != null) return false;
		if (constraints != null ? !constraints.equals(that.constraints) : that.constraints != null) return false;
		if (output != null ? !output.equals(that.output) : that.output != null) return false;

		return true;
	}

	public Collection<JudgeTestCasesEntity> getJudgeTestCasesById() {
		return judgeTestCasesById;
	}

	public void setJudgeTestCasesById(Collection<JudgeTestCasesEntity> judgeTestCasesById) {
		this.judgeTestCasesById = judgeTestCasesById;
	}
}
