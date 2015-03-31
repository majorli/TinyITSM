package com.jeans.tinyitsm.model.it.post;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import com.jeans.tinyitsm.model.it.Wiki;

@Entity
@Table(name = "wiki_pages")
@PrimaryKeyJoinColumn(name = "id")
@Indexed
public class Page extends Post {

	private String title;
	private Wiki wiki;
	private List<WikiReply> replies = new ArrayList<WikiReply>();

	@Column(nullable = false, length = 255)
	@Field
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "wiki_id", foreignKey = @ForeignKey(name = "FK_PAGE_WIKI"))
	public Wiki getWiki() {
		return wiki;
	}

	public void setWiki(Wiki wiki) {
		this.wiki = wiki;
	}

	@OneToMany(mappedBy = "page")
	public List<WikiReply> getReplies() {
		return replies;
	}

	public void setReplies(List<WikiReply> replies) {
		this.replies = replies;
	}

	public void addReply(WikiReply reply) {
		this.replies.add(reply);
	}

	public void removeReply(WikiReply reply) {
		this.replies.remove(reply);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((replies == null) ? 0 : replies.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((wiki == null) ? 0 : wiki.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Page other = (Page) obj;
		if (replies == null) {
			if (other.replies != null)
				return false;
		} else if (!replies.equals(other.replies))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (wiki == null) {
			if (other.wiki != null)
				return false;
		} else if (!wiki.equals(other.wiki))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Page [title=").append(title).append(", wiki=").append(wiki.getName()).append("]");
		return builder.toString();
	}
}
