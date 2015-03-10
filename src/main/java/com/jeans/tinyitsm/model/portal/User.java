package com.jeans.tinyitsm.model.portal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.jeans.tinyitsm.util.EncodeUtil;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private long id;
	private long companyId;
	private long employeeId;
	private String password;
	private boolean iter;
	private boolean admin;
	private String username;
	private boolean available;
	private boolean leader;
	private boolean supervisor;
	private boolean auditor;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false, name = "company_id")
	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	@Column(nullable = false, updatable = false, name = "employee_id")
	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	@Column(nullable = false, length = 40)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(nullable = false, name = "is_iter")
	public boolean isIter() {
		return iter;
	}

	public void setIter(boolean iter) {
		this.iter = iter;
	}

	@Column(nullable = false, name = "is_admin")
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Column(nullable = false, name = "username", length = 30)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(nullable = false, name = "is_available")
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isLeader() {
		return leader;
	}

	@Column(nullable = false)
	public void setLeader(boolean leader) {
		this.leader = leader;
	}

	public boolean isSupervisor() {
		return supervisor;
	}

	@Column(nullable = false)
	public void setSupervisor(boolean supervisor) {
		this.supervisor = supervisor;
	}

	public boolean isAuditor() {
		return auditor;
	}

	@Column(nullable = false)
	public void setAuditor(boolean auditor) {
		this.auditor = auditor;
	}

	public void encodePassword() {
		this.password = EncodeUtil.encodeBySHA1(this.password);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (admin ? 1231 : 1237);
		result = prime * result + (auditor ? 1231 : 1237);
		result = prime * result + (available ? 1231 : 1237);
		result = prime * result + (int) (companyId ^ (companyId >>> 32));
		result = prime * result + (int) (employeeId ^ (employeeId >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (iter ? 1231 : 1237);
		result = prime * result + (leader ? 1231 : 1237);
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + (supervisor ? 1231 : 1237);
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (admin != other.admin)
			return false;
		if (auditor != other.auditor)
			return false;
		if (available != other.available)
			return false;
		if (companyId != other.companyId)
			return false;
		if (employeeId != other.employeeId)
			return false;
		if (id != other.id)
			return false;
		if (iter != other.iter)
			return false;
		if (leader != other.leader)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (supervisor != other.supervisor)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=").append(id).append(", companyId=").append(companyId).append(", employeeId=").append(employeeId).append(", password=")
				.append(password).append(", iter=").append(iter).append(", admin=").append(admin).append(", username=").append(username).append(", available=")
				.append(available).append(", leader=").append(leader).append(", supervisor=").append(supervisor).append(", auditor=").append(auditor)
				.append("]");
		return builder.toString();
	}
}
