package com.infy.eta.databeans;

/**
 * Created by Amit Joshi on 10/22/2015.
 */
public class JudgeUserSolvedProblems {
	private Integer       id;
	private String        username;
	private Integer       problemId;
	private JudgeProblems judgeProblemsByProblemId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getProblemId() {
		return problemId;
	}

	public void setProblemId(Integer problemId) {
		this.problemId = problemId;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (username != null ? username.hashCode() : 0);
		result = 31 * result + problemId;
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JudgeUserSolvedProblems that = (JudgeUserSolvedProblems) o;

		if (id != that.id) return false;
		if (problemId != that.problemId) return false;
		if (username != null ? !username.equals(that.username) : that.username != null) return false;

		return true;
	}

	public JudgeProblems getJudgeProblemsByProblemId() {
		return judgeProblemsByProblemId;
	}

	public void setJudgeProblemsByProblemId(JudgeProblems judgeProblemsByProblemId) {
		this.judgeProblemsByProblemId = judgeProblemsByProblemId;
	}
}
