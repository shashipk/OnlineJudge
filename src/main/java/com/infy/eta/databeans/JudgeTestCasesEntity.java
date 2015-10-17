package com.infy.eta.databeans;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Amit Joshi on 10/4/2015.
 */
@XmlRootElement
public class JudgeTestCasesEntity {
	private Integer id;
	private Integer problemId;
	private String description;
	private String input;
	private String output;
	private Integer points;

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProblemId() {
		return problemId;
	}

	public void setProblemId(Integer problemId) {
		this.problemId = problemId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
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
		result = 31 * result + (problemId != null ? problemId.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (input != null ? input.hashCode() : 0);
		result = 31 * result + (output != null ? output.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JudgeTestCasesEntity that = (JudgeTestCasesEntity) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (problemId != null ? !problemId.equals(that.problemId) : that.problemId != null) return false;
		if (description != null ? !description.equals(that.description) : that.description != null) return false;
		if (input != null ? !input.equals(that.input) : that.input != null) return false;
		return !(output != null ? !output.equals(that.output) : that.output != null);

	}
}
