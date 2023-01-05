package net.poweregg.mitsubishi.dto;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalLinkageRequest {
	private String system;

	private String database;

	private String screen;

	private String event;

	private String process;

	private LinkedHashMap<String, JsonObjectDtl> record;

	private LoginUserExternalLinkage loginUser;

	public ExternalLinkageRequest() {
		super();
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public LinkedHashMap<String, JsonObjectDtl> getRecord() {
		return record;
	}

	public void setRecord(LinkedHashMap<String, JsonObjectDtl> record) {
		this.record = record;
	}

	public LoginUserExternalLinkage getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(LoginUserExternalLinkage loginUser) {
		this.loginUser = loginUser;
	}

}
