package com.jeans.tinyitsm.action.cloud;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.TinyAction;
import com.jeans.tinyitsm.model.view.CloudTreeNode;
import com.jeans.tinyitsm.service.cloud.CloudService;

public class UploadAction extends TinyAction {

	private CloudService service;

	@Autowired
	public void setService(CloudService service) {
		this.service = service;
	}

	private List<File> uploads = new ArrayList<File>();
	private List<String> uploadFileNames = new ArrayList<String>();
	private List<String> uploadContentTypes = new ArrayList<String>();

	public List<File> getUpload() {
		return this.uploads;
	}

	public void setUpload(List<File> uploads) {
		this.uploads = uploads;
	}

	public List<String> getUploadFileName() {
		return this.uploadFileNames;
	}

	public void setUploadFileName(List<String> uploadFileNames) {
		this.uploadFileNames = uploadFileNames;
	}

	public List<String> getUploadContentType() {
		return this.uploadContentTypes;
	}

	public void setUploadContentType(List<String> contentTypes) {
		this.uploadContentTypes = contentTypes;
	}

	private long listId;

	public long getListId() {
		return listId;
	}

	public void setListId(long listId) {
		this.listId = listId;
	}

	private Map<String, Object> results = new HashMap<String, Object>();

	public Map<String, Object> getResults() {
		return results;
	}

	public void setResults(Map<String, Object> results) {
		this.results = results;
	}

	@Action(value = "upload", results = { @Result(type = "json", params = { "root", "results", "contentType", "text/plain", "encoding", "UTF-8" }) })
	public String execute() throws Exception {
		String rootPath = ServletActionContext.getServletContext().getRealPath(getEnvironment().getCloudRoot());
		List<CloudTreeNode> succ = new ArrayList<CloudTreeNode>();
		List<String> fail = new ArrayList<String>();
		for (int i = 0; i < uploads.size(); i++) {
			try {
				CloudTreeNode node = service.upload(uploads.get(i), listId, uploadFileNames.get(i), uploadContentTypes.get(i), rootPath);
				if (null == succ) {
					fail.add(uploadFileNames.get(i));
				} else {
					succ.add(node);
				}
			} catch (IOException e) {
				log(e);
				fail.add(uploadFileNames.get(i));
			}
		}
		service.uploadRss(succ, listId);
		
		results.put("s", succ);
		results.put("f", fail);
		return SUCCESS;
	}
}
