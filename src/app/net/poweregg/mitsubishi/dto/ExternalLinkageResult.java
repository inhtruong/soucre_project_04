package net.poweregg.mitsubishi.dto;

import java.util.List;

public class ExternalLinkageResult {

	private String result;

	private List<String> messages;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

}
