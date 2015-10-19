package com.infy.eta.databeans;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Amit Joshi on 10/19/2015.
 */
public class JudgeContestsEntity {
	private Integer                         contestId;
	private String                          contestName;
	private String                          contestDescription;
	private String                          contestPrize;
	private String                          contestOrganizer;
	private int                             contestDuration;
	private Timestamp                       inZ;
	private Timestamp                       outZ;
	private Collection<JudgeProblemsEntity> judgeProblemsesByContestId;

	public Integer getContestId() {
		return contestId;
	}

	public void setContestId(Integer contestId) {
		this.contestId = contestId;
	}

	public String getContestName() {
		return contestName;
	}

	public void setContestName(String contestName) {
		this.contestName = contestName;
	}

	public String getContestDescription() {
		return contestDescription;
	}

	public void setContestDescription(String contestDescription) {
		this.contestDescription = contestDescription;
	}

	public String getContestPrize() {
		return contestPrize;
	}

	public void setContestPrize(String contestPrize) {
		this.contestPrize = contestPrize;
	}

	public String getContestOrganizer() {
		return contestOrganizer;
	}

	public void setContestOrganizer(String contestOrganizer) {
		this.contestOrganizer = contestOrganizer;
	}

	public int getContestDuration() {
		return contestDuration;
	}

	public void setContestDuration(int contestDuration) {
		this.contestDuration = contestDuration;
	}

	public Timestamp getInZ() {
		return inZ;
	}

	public void setInZ(Timestamp inZ) {
		this.inZ = inZ;
	}

	public Timestamp getOutZ() {
		return outZ;
	}

	public void setOutZ(Timestamp outZ) {
		this.outZ = outZ;
	}

	@Override
	public int hashCode() {
		int result = contestId != null ? contestId.hashCode() : 0;
		result = 31 * result + (contestName != null ? contestName.hashCode() : 0);
		result = 31 * result + (contestDescription != null ? contestDescription.hashCode() : 0);
		result = 31 * result + (contestPrize != null ? contestPrize.hashCode() : 0);
		result = 31 * result + (contestOrganizer != null ? contestOrganizer.hashCode() : 0);
		result = 31 * result + contestDuration;
		result = 31 * result + (inZ != null ? inZ.hashCode() : 0);
		result = 31 * result + (outZ != null ? outZ.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JudgeContestsEntity that = (JudgeContestsEntity) o;

		if (contestDuration != that.contestDuration) return false;
		if (contestId != null ? !contestId.equals(that.contestId) : that.contestId != null) return false;
		if (contestName != null ? !contestName.equals(that.contestName) : that.contestName != null) return false;
		if (contestDescription != null ? !contestDescription.equals(that.contestDescription) : that.contestDescription != null)
			return false;
		if (contestPrize != null ? !contestPrize.equals(that.contestPrize) : that.contestPrize != null) return false;
		if (contestOrganizer != null ? !contestOrganizer.equals(that.contestOrganizer) : that.contestOrganizer != null)
			return false;
		if (inZ != null ? !inZ.equals(that.inZ) : that.inZ != null) return false;
		if (outZ != null ? !outZ.equals(that.outZ) : that.outZ != null) return false;

		return true;
	}

	public Collection<JudgeProblemsEntity> getJudgeProblemsesByContestId() {
		return judgeProblemsesByContestId;
	}

	public void setJudgeProblemsesByContestId(Collection<JudgeProblemsEntity> judgeProblemsesByContestId) {
		this.judgeProblemsesByContestId = judgeProblemsesByContestId;
	}
}
