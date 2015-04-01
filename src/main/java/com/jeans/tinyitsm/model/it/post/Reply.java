package com.jeans.tinyitsm.model.it.post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "wiki_replies")
@PrimaryKeyJoinColumn(name = "id")
public class Reply extends Post {

	private Page page;
	private int listOrder;

	@ManyToOne
	@JoinColumn(nullable = false, name = "page_id", foreignKey = @ForeignKey(name = "FK_PAGE_REPLY"))
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Column(nullable = false, name = "list_order")
	public int getListOrder() {
		return listOrder;
	}

	public void setListOrder(int listOrder) {
		this.listOrder = listOrder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + listOrder;
		result = prime * result + ((page == null) ? 0 : page.hashCode());
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
		Reply other = (Reply) obj;
		if (listOrder != other.listOrder)
			return false;
		if (page == null) {
			if (other.page != null)
				return false;
		} else if (!page.equals(other.page))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Reply [page=").append(page.getTitle()).append(", listOrder=").append(listOrder).append("]");
		return builder.toString();
	}
}
