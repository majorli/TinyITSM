package com.jeans.tinyitsm.model.cloud;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.jeans.tinyitsm.model.CloudUnit;
import com.jeans.tinyitsm.model.portal.User;

@Entity
@Table(name = "cloud_lists")
@Indexed
public class CloudList implements CloudUnit {

	private long id;
	private String name;
	private User owner;
	private Set<User> permittedReaders = new HashSet<User>();
	private boolean privateUnit;
	private Set<Tag> tags = new HashSet<Tag>();
	private Date createTime;
	private Date lastUpdateTime;

	private List<CloudFile> files = new ArrayList<CloudFile>();
	private long subscribeCount;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	@Column(nullable = false)
	@Field
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	@JSON(serialize = false)
	@ManyToOne
	@JoinColumn(nullable = false, name = "owner_id", foreignKey = @ForeignKey(name = "FK_LIST_OWNER"))
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Override
	@JSON(serialize = false)
	@ManyToMany
	@JoinTable(name = "lists_permitted_users", joinColumns = { @JoinColumn(nullable = false, name = "list_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id") })
	public Set<User> getPermittedReaders() {
		return permittedReaders;
	}

	public void setPermittedReaders(Set<User> permittedReaders) {
		this.permittedReaders = permittedReaders;
	}

	@Override
	@Column(nullable = false, name = "private")
	public boolean isPrivateUnit() {
		return privateUnit;
	}

	public void setPrivateUnit(boolean privateUnit) {
		this.privateUnit = privateUnit;
	}

	@Override
	@ManyToMany
	@JoinTable(name = "lists_tags", joinColumns = { @JoinColumn(nullable = false, name = "list_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(nullable = false, name = "tag_id", referencedColumnName = "id") })
	@IndexedEmbedded
	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@Override
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	@Column(nullable = false, name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	@Column(nullable = false, name = "update_time")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@JSON(serialize = false)
	@OneToMany(mappedBy = "list")
	public List<CloudFile> getFiles() {
		return files;
	}

	public void setFiles(List<CloudFile> files) {
		this.files = files;
	}

	@Column(nullable = false, name = "subscribe_count")
	public long getSubscribeCount() {
		return subscribeCount;
	}

	public void setSubscribeCount(long subscribeCount) {
		this.subscribeCount = subscribeCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((files == null) ? 0 : files.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((lastUpdateTime == null) ? 0 : lastUpdateTime.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((permittedReaders == null) ? 0 : permittedReaders.hashCode());
		result = prime * result + (privateUnit ? 1231 : 1237);
		result = prime * result + (int) (subscribeCount ^ (subscribeCount >>> 32));
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
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
		CloudList other = (CloudList) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (files == null) {
			if (other.files != null)
				return false;
		} else if (!files.equals(other.files))
			return false;
		if (id != other.id)
			return false;
		if (lastUpdateTime == null) {
			if (other.lastUpdateTime != null)
				return false;
		} else if (!lastUpdateTime.equals(other.lastUpdateTime))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (permittedReaders == null) {
			if (other.permittedReaders != null)
				return false;
		} else if (!permittedReaders.equals(other.permittedReaders))
			return false;
		if (privateUnit != other.privateUnit)
			return false;
		if (subscribeCount != other.subscribeCount)
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CloudList [id=").append(id).append(", name=").append(name).append(", owner=").append(owner.getUsername()).append(", privateUnit=")
				.append(privateUnit).append(", createTime=").append(createTime).append(", lastUpdateTime=").append(lastUpdateTime).append(", subscribeCount=")
				.append(subscribeCount).append("]");
		return builder.toString();
	}
}
