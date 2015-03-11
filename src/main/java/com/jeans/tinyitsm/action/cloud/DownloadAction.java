package com.jeans.tinyitsm.action.cloud;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.TinyAction;
import com.jeans.tinyitsm.service.cloud.CloudService;

public class DownloadAction extends TinyAction {
	private CloudService service;

	@Autowired
	public void setService(CloudService service) {
		this.service = service;
	}

	private List<File> files;

	private long id;
	private String filename;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public InputStream getInputStream() throws FileNotFoundException {
		if (files.size() == 0 || null == filename) {
			return null;
		} else {
			return new FileInputStream(files.get(0));
		}
	}

	private String getRootPath() {
		return ServletActionContext.getServletContext().getRealPath(getEnvironment().getCloudRoot());
	}

	private boolean isIE() {
		String ua = ServletActionContext.getRequest().getHeader("user-agent").toUpperCase();
		if ((ua != null) && (ua.indexOf("MSIE") != -1 || ua.indexOf("TRIDENT") != -1)) {
			return true;
		} else {
			return false;
		}
	}

	@Action(value = "download", results = { @Result(name = SUCCESS, type = "stream", params = { "contentType", "application/octet-stream", "inputName",
			"inputStream", "contentDisposition", "attachment;filename=\"${filename}\"", "bufferSize", "4096" }) })
	public String download() throws Exception {
		files = new ArrayList<File>();
		filename = service.prepareDownload(id, getRootPath(), isIE(), files, getCurrentUser());
		if (files.size() == 0 || null == filename) {
			HttpServletResponse resp = ServletActionContext.getResponse();
			resp.setContentType("text/html; charset=UTF-8");
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			out.write("<script>parent.$.msgbox('错误', '文件不存在或没有访问权限，无法下载', 'warning');</script>");
			out.close();
			return null;
		} else {
			return SUCCESS;
		}
	}

	/**
	 * 栏目或收藏夹下载时前端传来的参数，表示类型
	 */
	private byte type;

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	private Map<String, File> entries;
	
	private void writeZipFile(HttpServletResponse resp) throws IOException {
		resp.setContentType("application/zip");
		resp.setHeader("Content-disposition", "attachment; filename=" + filename);
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(resp.getOutputStream(), 4096), Charset.forName("GBK"));
		byte[] buffer = new byte[4096];
		for (String fn : entries.keySet()) {
			File f = entries.get(fn);
			out.putNextEntry(new ZipEntry(fn));
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(f), 4096);
			int len = 0;
			while ((len = in.read(buffer)) != -1)
				out.write(buffer, 0, len);
			in.close();
			out.closeEntry();
			out.flush();
		}
		out.finish();
		out.close();
	}
	
	private void writeErrorMessage(HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.write("<script>parent.$.msgbox('错误', '所有文件均不存在或没有访问权限，无法下载', 'warning');</script>");
		out.close();
	}

	@Action(value = "download-list")
	public String downloadList() throws Exception {
		entries = new HashMap<String, File>();
		filename = service.prepareListDownload(id, type, getRootPath(), isIE(), entries, getCurrentUser());
		HttpServletResponse resp = ServletActionContext.getResponse();
		if (entries.size() == 0 || null == filename) {
			writeErrorMessage(resp);
		} else {
			writeZipFile(resp);
		}
		return null;
	}
	
	private String ids;

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	private List<Long> splitIds() {
		List<Long> ret = new ArrayList<Long>();
		if (!StringUtils.isBlank(ids)) {
			String[] stringIds = ids.trim().split(",");
			for (String id : stringIds) {
				try {
					ret.add(Long.parseLong(id.trim()));
				} catch (NumberFormatException e) {
				}
			}
		}
		return ret;
	}

	@Action(value = "download-multi")
	public String downloadMulti() throws Exception {
		entries = new HashMap<String, File>();
		filename = service.prepareMultiDownload(splitIds(), getRootPath(), isIE(), entries, getCurrentUser());
		HttpServletResponse resp = ServletActionContext.getResponse();
		if (entries.size() == 0 || null == filename) {
			writeErrorMessage(resp);
		} else {
			writeZipFile(resp);
		}
		return null;
	}
}
