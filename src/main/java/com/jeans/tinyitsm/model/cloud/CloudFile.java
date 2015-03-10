package com.jeans.tinyitsm.model.cloud;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedHashSet;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.jeans.tinyitsm.model.CloudUnit;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.util.FileUtil;

@Entity
@Table(name = "cloud_files")
@Indexed
public class CloudFile implements CloudUnit {

	private long id;
	private String name;
	private User owner;
	private Set<User> permittedReaders = new LinkedHashSet<User>();
	private boolean privateUnit;
	private Set<Tag> tags = new LinkedHashSet<Tag>();
	private Date createTime;
	private Date lastUpdateTime;

	private byte majorVersion;
	private double minorVersion;
	private String versionType;
	private String brief;
	private CloudList list;
	private long size;
	private String contentType;
	private long dlCount;
	private long fvCount;

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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	@JSON(serialize = false)
	@ManyToOne
	@JoinColumn(nullable = false, name = "owner_id", foreignKey = @ForeignKey(name = "FK_FILE_OWNER"))
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Override
	@JSON(serialize = false)
	@ManyToMany
	@JoinTable(name = "files_permitted_users", joinColumns = { @JoinColumn(nullable = false, name = "file_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id") })
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
	@JoinTable(name = "files_tags", joinColumns = { @JoinColumn(nullable = false, name = "file_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(nullable = false, name = "tag_id", referencedColumnName = "id") })
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
	@Column(nullable = false, name = "major_version")
	public byte getMajorVersion() {
		return majorVersion;
	}

	public void setMajorVersion(byte majorVersion) {
		this.majorVersion = majorVersion;
	}

	@JSON(serialize = false)
	@Column(nullable = false, name = "minor_version")
	public double getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(double minorVersion) {
		this.minorVersion = minorVersion;
	}

	@JSON(serialize = false)
	@Column(nullable = false, name = "version_type")
	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	@JSON(serialize = false)
	@Column(nullable = false, length = 512)
	@Field
	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	@JSON(serialize = false)
	@ManyToOne
	@JoinColumn(nullable = false, name = "list_id", foreignKey = @ForeignKey(name = "FK_LIST"))
	public CloudList getList() {
		return list;
	}

	public void setList(CloudList list) {
		this.list = list;
	}

	@Column(nullable = false)
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@JSON(serialize = false)
	@Column(nullable = false, name = "content_type")
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Column(nullable = false, name = "download_count")
	public long getDlCount() {
		return dlCount;
	}

	public void setDlCount(long dlCount) {
		this.dlCount = dlCount;
	}

	@Column(nullable = false, name = "favorite_count")
	public long getFvCount() {
		return fvCount;
	}

	public void setFvCount(long fvCount) {
		this.fvCount = fvCount;
	}

	/**
	 * 获取文件的版本号，文件上传时默认没有版本号，即majorVersion == 0, minorVersion == 0.0, versionType == ""<br>
	 * minorVersion的整数部分不能大于9，大于9的进位到majorVersion
	 * versionType在CloudConstants.VERSION_TYPES常量数组中有预定义值若干，也可以为空，也可以自定义<br>
	 * 
	 * @return
	 */
	@Transient
	public String getVersion() {
		if (majorVersion == 0 && minorVersion == 0.0) {
			return "";
		}
		DecimalFormat df = new DecimalFormat("0.0#");
		StringBuilder ver = new StringBuilder();
		ver.append(majorVersion).append(".").append(df.format(minorVersion));
		if (!StringUtils.isBlank(versionType)) {
			ver.append("-").append(versionType);
		}
		return ver.toString();
	}

	/**
	 * 根据dev升级版本号
	 * 
	 * @param dev
	 *            版本号差异，dev的取值范围为{ 1.0, 0.1, 0.01 }
	 */
	public void upgrade(double dev) {
		if (dev == 1.0) {
			if (majorVersion < 127) {
				majorVersion++;
			}
		} else if (dev == 0.1) {
			minorVersion += 1.0;
			if (minorVersion >= 100.0 && majorVersion < 127) {
				minorVersion -= 100.0;
				majorVersion++;
			}
		} else if (dev == 0.01) {
			minorVersion += 0.01;
			if (minorVersion >= 100.0 && majorVersion < 127) {
				minorVersion -= 100.0;
				majorVersion++;
			}
		}
	}

	/**
	 * 设置版本的便捷方法
	 * 
	 * @param major
	 *            主版本号
	 * @param minor
	 *            次版本号
	 */
	public void upgradeTo(byte major, double minor) {
		majorVersion = major;
		minorVersion = minor;
		if (minorVersion >= 100.0 && majorVersion < 127) {
			minorVersion -= 100.0;
			majorVersion++;
		}
	}

	/**
	 * 获取相对于ContextPath的完整相对路径
	 * 所有文件的实际物理存放都以id为文件名，没有扩展名，存放在{ContextPath}/{cloudRoot}/{xxx}目录下，xxx为子目录名，每个子目录里存放最多1000个文件
	 * 所以xxx = id / 1000 (整数除法)
	 * 
	 * @return
	 */
	@Transient
	@JSON(serialize = false)
	public String getFullPath() {
		return "/" + (id / 1000);
	}

	@Transient
	@Field
	@JSON(serialize = false)
	public String getVersionFilename() {
		StringBuilder builder = new StringBuilder(name);
		String ver = getVersion();
		if (!StringUtils.isBlank(ver)) {
			int p = builder.lastIndexOf(".");
			if (p == -1) {
				builder.append(" (" + ver + ")");
			} else {
				builder.insert(p, " (" + ver + ")");
			}
		}
		return builder.toString();
	}

	/**
	 * 获取iconCls的类名
	 * 
	 * @return
	 */
	@Transient
	public String getIconCls() {
		return FileUtil.getFileIconCls(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + (int) (dlCount ^ (dlCount >>> 32));
		result = prime * result + (int) (fvCount ^ (fvCount >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((lastUpdateTime == null) ? 0 : lastUpdateTime.hashCode());
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + majorVersion;
		long temp;
		temp = Double.doubleToLongBits(minorVersion);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((permittedReaders == null) ? 0 : permittedReaders.hashCode());
		result = prime * result + (privateUnit ? 1231 : 1237);
		result = prime * result + (int) (size ^ (size >>> 32));
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((versionType == null) ? 0 : versionType.hashCode());
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
		CloudFile other = (CloudFile) obj;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (dlCount != other.dlCount)
			return false;
		if (fvCount != other.fvCount)
			return false;
		if (id != other.id)
			return false;
		if (lastUpdateTime == null) {
			if (other.lastUpdateTime != null)
				return false;
		} else if (!lastUpdateTime.equals(other.lastUpdateTime))
			return false;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		if (majorVersion != other.majorVersion)
			return false;
		if (Double.doubleToLongBits(minorVersion) != Double.doubleToLongBits(other.minorVersion))
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
		if (size != other.size)
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (versionType == null) {
			if (other.versionType != null)
				return false;
		} else if (!versionType.equals(other.versionType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CloudFile [id=").append(id).append(", name=").append(name).append(", owner=").append(owner.getUsername()).append(", privateUnit=")
				.append(privateUnit).append(", createTime=").append(createTime).append(", lastUpdateTime=").append(lastUpdateTime).append(", majorVersion=")
				.append(majorVersion).append(", minorVersion=").append(minorVersion).append(", versionType=").append(versionType).append(", list=")
				.append(list.getName()).append(", size=").append(size).append(", contentType=").append(contentType).append(", dlCount=").append(dlCount)
				.append(", fvCount=").append(fvCount).append("]");
		return builder.toString();
	}
}
