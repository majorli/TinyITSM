package com.jeans.tinyitsm.model.cloud;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

import com.jeans.tinyitsm.model.portal.User;

/**
 * 文件收藏
 * 
 * @author Majorli
 *
 */
@Entity
@Table(name = "favorites")
public class Favorite implements Serializable {

	private long id;
	private User user;
	private CloudFile file;
	private FavorList list;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JSON(serialize = false)
	@ManyToOne
	@JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "FK_FAVOR_USER"))
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JSON(serialize = false)
	@ManyToOne
	@JoinColumn(nullable = false, name = "file_id", foreignKey = @ForeignKey(name = "FK_FAVOR_FILE"))
	public CloudFile getFile() {
		return file;
	}

	public void setFile(CloudFile file) {
		this.file = file;
	}

	@JSON(serialize = false)
	@ManyToOne
	@JoinColumn(nullable = false, name = "favor_list_id", foreignKey = @ForeignKey(name = "FK_FAVOR_LIST"))
	public FavorList getList() {
		return list;
	}

	public void setList(FavorList list) {
		this.list = list;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Favorite other = (Favorite) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (id != other.id)
			return false;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Favorite [id=").append(id).append(", user=").append(user.getUsername()).append(", file=").append(file.getName()).append(", list=")
				.append(list.getName()).append("]");
		return builder.toString();
	}
}
