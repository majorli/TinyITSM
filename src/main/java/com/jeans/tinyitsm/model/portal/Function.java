package com.jeans.tinyitsm.model.portal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "functions", uniqueConstraints = { @UniqueConstraint(name = "UK_code", columnNames = { "code" }) })
public class Function implements Serializable {

	/**
	 * folder == "" : this is a folder
	 * folder == "UTIL" : this function is a utility-function, not a menu-function, e.g. /portal/home.jsp
	 * folder == "PORTAL","SERVICE","ASSET","CLOUD","PROJECT","MAINTAIN",... : menu-functions, will be shown in navigator panel
	 * title == "" : this function will not be shown in navigator panel
	 */
	private long id;
	private String folder;
	private String code;
	private String title;
	private String target;
	private short listOrder;
	private boolean adminFunction;
	private boolean iterFunction;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false, length = 20)
	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	@NaturalId
	@Column(nullable = false, length = 20)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable = false, length = 20)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false)
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Column(nullable = false, name = "list_order")
	public short getListOrder() {
		return listOrder;
	}

	public void setListOrder(short listOrder) {
		this.listOrder = listOrder;
	}

	@Column(nullable = false, name = "admin_func")
	public boolean isAdminFunction() {
		return adminFunction;
	}

	public void setAdminFunction(boolean adminFunction) {
		this.adminFunction = adminFunction;
	}

	@Column(nullable = false, name = "iter_func")
	public boolean isIterFunction() {
		return iterFunction;
	}

	public void setIterFunction(boolean iterFunction) {
		this.iterFunction = iterFunction;
	}

	@Override
	public String toString() {
		return "Function [id=" + id + ", folder=" + folder + ", code=" + code + ", title=" + title + ", target=" + target + ", listOrder=" + listOrder
				+ ", adminFunction=" + adminFunction + ", iterFunction=" + iterFunction + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (adminFunction ? 1231 : 1237);
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((folder == null) ? 0 : folder.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (iterFunction ? 1231 : 1237);
		result = prime * result + listOrder;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Function other = (Function) obj;
		if (adminFunction != other.adminFunction)
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (folder == null) {
			if (other.folder != null)
				return false;
		} else if (!folder.equals(other.folder))
			return false;
		if (id != other.id)
			return false;
		if (iterFunction != other.iterFunction)
			return false;
		if (listOrder != other.listOrder)
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
}
