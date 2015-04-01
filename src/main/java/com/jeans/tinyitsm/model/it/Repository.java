package com.jeans.tinyitsm.model.it;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.jeans.tinyitsm.model.cloud.CloudList;

@Entity
@Table(name = "repositories")
public class Repository implements Serializable {
	private long id;
	private Set<CloudList> lists = new HashSet<CloudList>();
}
