package net.poweregg.mitsubishi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JsonObjectDtl.java<br>
 * JsonObjectDtl.<br>
 * .<br>
 * 
 * <pre>
 *  [変更履歴]
 * 1.0 2020/11/17 初版
 * </pre>
 * 
 * @version 1.0
 * @author thaint
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonObjectDtl {
	@JsonProperty(value = "corpCode")
	private String corpCode;

	@JsonProperty(value = "deptCode")
	private String deptCode;

	@JsonProperty(value = "name")
	private String name;

	public JsonObjectDtl() {
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
