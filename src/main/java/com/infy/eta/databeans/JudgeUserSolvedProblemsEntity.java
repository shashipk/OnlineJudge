package com.infy.eta.databeans;

/**
 * Created by Amit Joshi on 10/19/2015.
 */
public class JudgeUserSolvedProblemsEntity {
	private Integer             id;
	private String              username;
	private Integer             problemId;
	private JudgeProblemsEntity judgeProblemsByProblemId;

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
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (username != null ? username.hashCode() : 0);
		result = 31 * result + (problemId != null ? problemId.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JudgeUserSolvedProblemsEntity that = (JudgeUserSolvedProblemsEntity) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (username != null ? !username.equals(that.username) : that.username != null) return false;
		if (problemId != null ? !problemId.equals(that.problemId) : that.problemId != null) return false;

		return true;
	}

	public JudgeProblemsEntity getJudgeProblemsByProblemId() {
		return judgeProblemsByProblemId;
	}

	public void setJudgeProblemsByProblemId(JudgeProblemsEntity judgeProblemsByProblemId) {
		this.judgeProblemsByProblemId = judgeProblemsByProblemId;
	}
}
