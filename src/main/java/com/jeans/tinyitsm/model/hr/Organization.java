package com.jeans.tinyitsm.model.hr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "organizations")
public class Organization implements Serializable {

	private long id;
	private String name;
	private String alias;
	private byte type;
	private byte state;
	private short listOrder;
	private List<Employee> employees = new ArrayList<Employee>();
	private Organization superior;
	private List<Organization> inferiors = new ArrayList<Organization>();
	private Organization subRoot;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false, length = 30)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 15)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(nullable = false)
	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	@Column(nullable = false)
	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	@Column(nullable = false, name = "list_order")
	public short getListOrder() {
		return listOrder;
	}

	public void setListOrder(short listOrder) {
		this.listOrder = listOrder;
	}

	@OneToMany(mappedBy = "department")
	@OrderBy("state, listOrder, id")
	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "superior_id", foreignKey = @ForeignKey(name = "FK_SUP_ORG"))
	public Organization getSuperior() {
		return superior;
	}

	public void setSuperior(Organization superior) {
		this.superior = superior;
	}

	@OneToMany(mappedBy = "superior")
	@OrderBy("type desc, state, listOrder, id")
	public List<Organization> getInferiors() {
		return inferiors;
	}

	public void setInferiors(List<Organization> inferiors) {
		this.inferiors = inferiors;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "sub_root_id", foreignKey = @ForeignKey(name = "FK_SUB_ROOT"))
	public Organization getSubRoot() {
		return subRoot;
	}

	public void setSubRoot(Organization company) {
		this.subRoot = company;
	}

	@Override
	public String toString() {
		return "Organization [id=" + id + ", name=" + name + ", alias=" + alias + ", type=" + type + ", state=" + state + ", listOrder=" + listOrder
				+ ", superior=" + superior + ", subRoot=" + subRoot + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + listOrder;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + state;
		result = prime * result + type;
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
		Organization other = (Organization) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (id != other.id)
			return false;
		if (listOrder != other.listOrder)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (state != other.state)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
