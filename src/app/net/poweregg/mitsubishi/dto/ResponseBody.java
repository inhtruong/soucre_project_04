package net.poweregg.mitsubishi.dto;

import java.util.List;

/********************************************************************************
 * ResponseBody
 * 
 * @author : minhhtp
 * @createDate : 2021/02/23
 *
 ********************************************************************************/
public class ResponseBody {

	private List<ErrorContent> messages;

	public ResponseBody() {

	}

	public List<ErrorContent> getMessages() {
		return messages;
	}

	public void setMessages(List<ErrorContent> messages) {
		this.messages = messages;
	}
}
