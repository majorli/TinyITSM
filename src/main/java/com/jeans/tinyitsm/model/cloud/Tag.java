package com.jeans.tinyitsm.model.cloud;

import java.io.Serializable;
import java.text.Collator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Table(name = "tags", uniqueConstraints = { @UniqueConstraint(name = "UK_title", columnNames = { "title" }) })
@Indexed
public class Tag implements Comparable<Tag>, Serializable {

	private long id;
	private String title;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false, length = 10)
	@Field
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Tag other = (Tag) obj;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tag [id=").append(id).append(", title=").append(title).append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Tag o) {
		String s1 = this.getTitle();
		String s2 = o.getTitle();
		
		if (null == s1) {
			if (null == s2) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (null == s2) {
				return 1;
			} else {
				return Collator.getInstance(java.util.Locale.CHINA).compare(s1, s2);
			}
		}
	}
}
