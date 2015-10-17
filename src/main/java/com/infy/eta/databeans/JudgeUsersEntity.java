package com.infy.eta.databeans;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Amit Joshi on 10/5/2015.
 */
@XmlRootElement
public class JudgeUsersEntity {
	private Integer id;
	private String  username;
	private String  password;
	private String  firstName;
	private String  lastName;
	private String  role;
	private Timestamp inZ;
	private Timestamp outZ;

	private Collection<JudgeProblemsEntity> judgeProblemsByUsername;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public Collection<JudgeProblemsEntity> getJudgeProblemsByUsername() {
		return judgeProblemsByUsername;
	}

	public void setJudgeProblemsByUsername(Collection<JudgeProblemsEntity> judgeProblemsByUsername) {
		this.judgeProblemsByUsername = judgeProblemsByUsername;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (username != null ? username.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (role != null ? role.hashCode() : 0);
		result = 31 * result + (inZ != null ? inZ.hashCode() : 0);
		result = 31 * result + (outZ != null ? outZ.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JudgeUsersEntity that = (JudgeUsersEntity) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (username != null ? !username.equals(that.username) : that.username != null) return false;
		if (password != null ? !password.equals(that.password) : that.password != null) return false;
		if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
		if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
		if (role != null ? !role.equals(that.role) : that.role != null) return false;
		if (inZ != null ? !inZ.equals(that.inZ) : that.inZ != null) return false;
		return !(outZ != null ? !outZ.equals(that.outZ) : that.outZ != null);

	}
}
