package com.infy.eta.databeans;

/**
 * Created by Amit Joshi on 10/22/2015.
 */
public class JudgeTestCases {
	private Integer id;
	private Integer problemId;
	private String description;
	private String input;
	private String output;
	private Integer points;
	private JudgeProblems judgeProblemsByProblemId;

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

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + problemId;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (input != null ? input.hashCode() : 0);
		result = 31 * result + (output != null ? output.hashCode() : 0);
		result = 31 * result + points;
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JudgeTestCases that = (JudgeTestCases) o;

		if (id != that.id) return false;
		if (problemId != that.problemId) return false;
		if (points != that.points) return false;
		if (description != null ? !description.equals(that.description) : that.description != null) return false;
		if (input != null ? !input.equals(that.input) : that.input != null) return false;
		if (output != null ? !output.equals(that.output) : that.output != null) return false;

		return true;
	}

	public JudgeProblems getJudgeProblemsByProblemId() {
		return judgeProblemsByProblemId;
	}

	public void setJudgeProblemsByProblemId(JudgeProblems judgeProblemsByProblemId) {
		this.judgeProblemsByProblemId = judgeProblemsByProblemId;
	}
}
